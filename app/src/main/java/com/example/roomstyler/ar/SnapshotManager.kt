package com.example.roomstyler.ar

import android.content.Context
import android.graphics.Bitmap
import com.example.roomstyler.ar.model.ArScene
import com.example.roomstyler.data.mapper.SnapshotModel
import com.example.roomstyler.domain.repository.SnapshotRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.sceneview.ar.ARSceneView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnapshotManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val snapshotRepository: SnapshotRepository
) {
    
    private val snapshotsDir = File(context.filesDir, "snapshots")
    private val thumbnailsDir = File(context.filesDir, "thumbnails")
    
    init {
        if (!snapshotsDir.exists()) {
            snapshotsDir.mkdirs()
        }
        if (!thumbnailsDir.exists()) {
            thumbnailsDir.mkdirs()
        }
    }
    
    suspend fun captureSnapshot(
        arSceneView: ARSceneView,
        arScene: ArScene,
        layoutId: String,
        name: String = "スナップショット",
        description: String? = null
    ): Result<SnapshotModel> = withContext(Dispatchers.IO) {
        try {
            val snapshotId = UUID.randomUUID().toString()
            
            // Capture AR scene as bitmap
            val bitmap = captureArSceneBitmap(arSceneView)
                ?: return@withContext Result.failure(Exception("ARシーンのキャプチャに失敗しました"))
            
            // Save full image
            val imagePath = saveImage(bitmap, snapshotId, "snapshot")
            
            // Create and save thumbnail
            val thumbnail = createThumbnail(bitmap)
            val thumbnailPath = saveImage(thumbnail, snapshotId, "thumbnail")
            
            // Calculate room dimensions
            val roomDimensions = "${arScene.objects.size}個のオブジェクト"
            
            // Create snapshot model
            val snapshot = SnapshotModel(
                id = snapshotId,
                name = name,
                description = description,
                layoutId = layoutId,
                imagePath = imagePath,
                thumbnailPath = thumbnailPath,
                arScene = arScene,
                furnitureCount = arScene.objects.size,
                roomDimensions = roomDimensions,
                tags = listOf("AR", "スナップショット"),
                isFavorite = false
            )
            
            // Save to database
            snapshotRepository.insertSnapshot(snapshot)
            
            Result.success(snapshot)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun captureArSceneBitmap(arSceneView: ARSceneView): Bitmap? {
        return withContext(Dispatchers.Main) {
            try {
                // Try to capture actual ARSceneView screenshot
                return@withContext suspendCoroutine<Bitmap?> { continuation ->
                    try {
                        // Use PixelCopy to capture the ARSceneView content
                        val bitmap = Bitmap.createBitmap(
                            arSceneView.width,
                            arSceneView.height,
                            Bitmap.Config.ARGB_8888
                        )
                        
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            android.view.PixelCopy.request(
                                arSceneView as android.view.SurfaceView,
                                bitmap,
                                { result ->
                                    if (result == android.view.PixelCopy.SUCCESS) {
                                        continuation.resume(bitmap)
                                    } else {
                                        // Fallback to placeholder
                                        val fallbackBitmap = createFallbackBitmap()
                                        continuation.resume(fallbackBitmap)
                                    }
                                },
                                android.os.Handler(android.os.Looper.getMainLooper())
                            )
                        } else {
                            // For older Android versions, use fallback
                            val fallbackBitmap = createFallbackBitmap()
                            continuation.resume(fallbackBitmap)
                        }
                    } catch (e: Exception) {
                        val fallbackBitmap = createFallbackBitmap()
                        continuation.resume(fallbackBitmap)
                    }
                }
            } catch (e: Exception) {
                createFallbackBitmap()
            }
        }
    }
    
    private fun createFallbackBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        
        // Create a more informative placeholder
        canvas.drawColor(android.graphics.Color.parseColor("#E3F2FD"))
        
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#1976D2")
            textSize = 48f
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }
        
        canvas.drawText(
            "ARスナップショット",
            bitmap.width / 2f,
            bitmap.height / 2f - 50f,
            paint
        )
        
        paint.textSize = 32f
        canvas.drawText(
            "撮影日時: ${java.text.SimpleDateFormat("yyyy/MM/dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}",
            bitmap.width / 2f,
            bitmap.height / 2f + 50f,
            paint
        )
        
        return bitmap
    }
    
    private fun createThumbnail(bitmap: Bitmap, size: Int = 200): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val thumbnailWidth: Int
        val thumbnailHeight: Int
        
        if (aspectRatio > 1) {
            thumbnailWidth = size
            thumbnailHeight = (size / aspectRatio).toInt()
        } else {
            thumbnailWidth = (size * aspectRatio).toInt()
            thumbnailHeight = size
        }
        
        return Bitmap.createScaledBitmap(bitmap, thumbnailWidth, thumbnailHeight, true)
    }
    
    private fun saveImage(bitmap: Bitmap, id: String, type: String): String {
        val dir = if (type == "thumbnail") thumbnailsDir else snapshotsDir
        val file = File(dir, "${id}.png")
        
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        
        return file.absolutePath
    }
    
    suspend fun deleteSnapshot(snapshotId: String) = withContext(Dispatchers.IO) {
        try {
            val snapshot = snapshotRepository.getSnapshotById(snapshotId)
            if (snapshot != null) {
                // Delete image files
                File(snapshot.imagePath).delete()
                snapshot.thumbnailPath?.let { File(it).delete() }
                
                // Delete from database
                snapshotRepository.deleteSnapshotById(snapshotId)
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    suspend fun toggleFavorite(snapshotId: String) {
        try {
            val snapshot = snapshotRepository.getSnapshotById(snapshotId)
            if (snapshot != null) {
                snapshotRepository.updateFavoriteStatus(snapshotId, !snapshot.isFavorite)
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
}
