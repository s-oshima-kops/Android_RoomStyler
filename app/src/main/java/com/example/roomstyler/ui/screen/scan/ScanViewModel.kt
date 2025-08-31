package com.example.roomstyler.ui.screen.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomstyler.ar.ArSceneManager
import com.example.roomstyler.ar.model.ArPosition
import com.example.roomstyler.ar.repository.ArSceneRepository
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val arSceneManager: ArSceneManager,
    private val arSceneRepository: ArSceneRepository,
    private val snapshotManager: com.example.roomstyler.ar.SnapshotManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()
    
    private var arSceneView: ARSceneView? = null
    private val anchorNodes = mutableMapOf<String, AnchorNode>()
    
    init {
        viewModelScope.launch {
            combine(
                arSceneManager.currentScene,
                arSceneManager.selectedObjectId
            ) { scene, selectedId ->
                _uiState.value = _uiState.value.copy(
                    objectCount = scene.objects.size,
                    selectedObjectId = selectedId
                )
            }
        }
    }

    fun setupArSceneView(sceneView: ARSceneView) {
        arSceneView = sceneView
        
        try {
            sceneView.apply {
                // Enable plane detection
                planeRenderer.isEnabled = true
                planeRenderer.isVisible = true
            }
            
            _uiState.value = _uiState.value.copy(
                message = "ARSceneViewを初期化しました"
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = "ARSceneViewの初期化に失敗しました: ${e.message}"
            )
        }
    }
    
    // Manual tap handling method to be called from UI
    fun handleTap() {
        // For now, just add a box at center when tapped
        addBoxAtCenter()
    }
    
    private fun handleArTap(hitResult: HitResult) {
        val trackable = hitResult.trackable
        if (trackable is Plane && trackable.isPoseInPolygon(hitResult.hitPose)) {
            addBoxAtHitResult(hitResult)
        }
    }
    
    private fun addBoxAtHitResult(hitResult: HitResult) {
        val anchor = hitResult.createAnchor()
        val pose = hitResult.hitPose
        
        val position = ArPosition(
            x = pose.translation[0],
            y = pose.translation[1],
            z = pose.translation[2]
        )
        
        val objectId = arSceneManager.addObject("box", position)
        createAnchorNode(objectId, anchor)
    }
    
    fun addBoxAtCenter() {
        _uiState.value = _uiState.value.copy(
            message = "ボックスを追加しています..."
        )
        
        arSceneView?.let { sceneView ->
            try {
                // Add box at camera center, 1 meter forward
                val cameraPosition = sceneView.cameraNode.worldPosition
                
                // Calculate forward direction from rotation
                val forward = Position(0f, 0f, -1f) // Default forward direction
                
                val position = ArPosition(
                    x = cameraPosition.x + forward.x,
                    y = cameraPosition.y + forward.y - 0.5f, // Slightly lower
                    z = cameraPosition.z + forward.z
                )
                
                val objectId = arSceneManager.addObject("box", position)
                
                // Create anchor node without AR anchor (for demo purposes)
                createModelNode(objectId, position)
                
                // Update object count first
                val newCount = _uiState.value.objectCount + 1
                _uiState.value = _uiState.value.copy(
                    objectCount = newCount
                )
                
                // Then show success message with delay
                viewModelScope.launch {
                    kotlinx.coroutines.delay(100) // Small delay to ensure UI update
                    _uiState.value = _uiState.value.copy(
                        message = "✅ ボックス #${newCount} を追加しました (ID: $objectId)"
                    )
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "ボックスの追加に失敗しました: ${e.message}"
                )
            }
        } ?: run {
            _uiState.value = _uiState.value.copy(
                error = "ARSceneViewが初期化されていません"
            )
        }
    }
    
    private fun createAnchorNode(objectId: String, anchor: Anchor) {
        arSceneView?.let { sceneView ->
            val anchorNode = AnchorNode(sceneView.engine, anchor)
            
            // For now, create a simple placeholder node without model
            // TODO: Implement proper model loading in production
            
            sceneView.addChildNode(anchorNode)
            anchorNodes[objectId] = anchorNode
        }
    }
    
    private fun createModelNode(@Suppress("UNUSED_PARAMETER") objectId: String, position: ArPosition) {
        arSceneView?.let { sceneView ->
            try {
                // Create a simple node at the specified position
                val node = io.github.sceneview.node.Node(sceneView.engine)
                
                // Set position
                node.worldPosition = io.github.sceneview.math.Position(
                    position.x.toFloat(),
                    position.y.toFloat(),
                    position.z.toFloat()
                )
                
                // Add to scene
                sceneView.addChildNode(node)
                
                // Don't overwrite the main success message here
                // Just log the position for debugging if needed
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "オブジェクトを配置しました（データのみ）: $objectId"
                )
            }
        }
    }
    
    fun deleteSelectedObject() {
        val selectedId = arSceneManager.selectedObjectId.value
        if (selectedId != null) {
            // Remove from scene manager
            arSceneManager.deleteObject(selectedId)
            
            // Remove from AR scene
            anchorNodes[selectedId]?.let { anchorNode ->
                arSceneView?.removeChildNode(anchorNode)
                anchorNodes.remove(selectedId)
            }
            
            // Also check for model nodes without anchors
            // Note: Node identification will be handled differently in production
            arSceneView?.childNodes?.firstOrNull()?.let { node ->
                if (anchorNodes.isEmpty()) {
                    arSceneView?.removeChildNode(node)
                }
            }
        }
    }
    
    fun startAr() {
        _uiState.value = _uiState.value.copy(
            isArActive = true,
            error = null,
            message = "ARセッションを開始しています..."
        )
    }
    
    fun saveCurrentScene() {
        viewModelScope.launch {
            try {
                val currentScene = arSceneManager.currentScene.value
                arSceneRepository.saveScene(currentScene)
                
                _uiState.value = _uiState.value.copy(
                    message = "シーンを保存しました"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "保存に失敗しました: ${e.message}"
                )
            }
        }
    }
    
    fun clearScene() {
        // Clear from scene manager
        arSceneManager.clearScene()
        
        // Clear from AR scene
        anchorNodes.values.forEach { anchorNode ->
            arSceneView?.removeChildNode(anchorNode)
        }
        anchorNodes.clear()
        
        // Clear model nodes without anchors
        arSceneView?.childNodes?.toList()?.forEach { node ->
            arSceneView?.removeChildNode(node)
        }
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
    
    fun exportSceneToJson() {
        viewModelScope.launch {
            try {
                val currentScene = arSceneManager.currentScene.value
                val jsonString = arSceneRepository.exportSceneToJson(currentScene)
                
                _uiState.value = _uiState.value.copy(
                    exportedJson = jsonString,
                    message = "JSONエクスポート完了\n$jsonString"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "エクスポートに失敗しました: ${e.message}"
                )
            }
        }
    }
    
    fun importSceneFromJson(jsonString: String) {
        viewModelScope.launch {
            try {
                val scene = arSceneRepository.importSceneFromJson(jsonString)
                if (scene != null) {
                    // Clear current scene first
                    clearScene()
                    
                    // Load new scene
                    arSceneManager.loadScene(scene)
                    
                    // Recreate AR objects
                    scene.objects.forEach { arObject ->
                        createModelNode(arObject.id, arObject.position)
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        message = "シーンを読み込みました (${scene.objects.size}個のオブジェクト)"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "無効なJSONフォーマットです"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "インポートに失敗しました: ${e.message}"
                )
            }
        }
    }
    
    fun showLoadDialog() {
        _uiState.value = _uiState.value.copy(showLoadDialog = true)
    }
    
    fun hideLoadDialog() {
        _uiState.value = _uiState.value.copy(showLoadDialog = false)
    }
    
    fun captureSnapshot(name: String = "ARスナップショット", description: String? = null) {
        viewModelScope.launch {
            val sceneView = arSceneView
            if (sceneView != null) {
                try {
                    val currentScene = arSceneManager.currentScene.value
                    val result = snapshotManager.captureSnapshot(
                        arSceneView = sceneView,
                        arScene = currentScene,
                        layoutId = currentScene.id,
                        name = name,
                        description = description
                    )
                    
                    result.onSuccess { snapshot ->
                        _uiState.value = _uiState.value.copy(
                            message = "スナップショットを保存しました: ${snapshot.name}"
                        )
                    }.onFailure { error ->
                        _uiState.value = _uiState.value.copy(
                            error = "スナップショットの保存に失敗しました: ${error.message}"
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        error = "スナップショットの保存に失敗しました: ${e.message}"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    error = "ARシーンが初期化されていません"
                )
            }
        }
    }
}

data class ScanUiState(
    val isArActive: Boolean = false,
    val objectCount: Int = 0,
    val selectedObjectId: String? = null,
    val error: String? = null,
    val message: String? = null,
    val showLoadDialog: Boolean = false,
    val exportedJson: String? = null
)
