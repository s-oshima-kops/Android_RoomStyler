package com.example.roomstyler.ar.repository

import com.example.roomstyler.ar.model.ArScene
import kotlinx.coroutines.flow.Flow

interface ArSceneRepository {
    suspend fun saveScene(scene: ArScene)
    suspend fun loadScene(sceneId: String): ArScene?
    suspend fun getAllScenes(): Flow<List<ArScene>>
    suspend fun deleteScene(sceneId: String)
    suspend fun exportSceneToJson(scene: ArScene): String
    suspend fun importSceneFromJson(json: String): ArScene?
}
