package com.example.roomstyler.ar.repository

import android.content.Context
import com.example.roomstyler.ar.model.ArScene
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArSceneRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ArSceneRepository {
    
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    private val scenesDir = File(context.filesDir, "ar_scenes")
    
    init {
        if (!scenesDir.exists()) {
            scenesDir.mkdirs()
        }
    }
    
    override suspend fun saveScene(scene: ArScene) = withContext(Dispatchers.IO) {
        val file = File(scenesDir, "${scene.id}.json")
        val jsonString = json.encodeToString(scene)
        file.writeText(jsonString)
    }
    
    override suspend fun loadScene(sceneId: String): ArScene? = withContext(Dispatchers.IO) {
        val file = File(scenesDir, "$sceneId.json")
        if (!file.exists()) return@withContext null
        
        try {
            val jsonString = file.readText()
            json.decodeFromString<ArScene>(jsonString)
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun getAllScenes(): Flow<List<ArScene>> = flow {
        withContext(Dispatchers.IO) {
            val scenes = mutableListOf<ArScene>()
            
            scenesDir.listFiles { _, name -> name.endsWith(".json") }?.forEach { file ->
                try {
                    val jsonString = file.readText()
                    val scene = json.decodeFromString<ArScene>(jsonString)
                    scenes.add(scene)
                } catch (e: Exception) {
                    // Skip invalid files
                }
            }
            
            emit(scenes.sortedByDescending { it.lastModified })
        }
    }
    
    override suspend fun deleteScene(sceneId: String) = withContext(Dispatchers.IO) {
        val file = File(scenesDir, "$sceneId.json")
        if (file.exists()) {
            file.delete()
        }
    }
    
    override suspend fun exportSceneToJson(scene: ArScene): String = withContext(Dispatchers.IO) {
        json.encodeToString(scene)
    }
    
    override suspend fun importSceneFromJson(json: String): ArScene? = withContext(Dispatchers.IO) {
        try {
            this@ArSceneRepositoryImpl.json.decodeFromString<ArScene>(json)
        } catch (e: Exception) {
            null
        }
    }
}
