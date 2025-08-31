package com.example.roomstyler.ar

import com.example.roomstyler.ar.model.ArObject
import com.example.roomstyler.ar.model.ArPosition
import com.example.roomstyler.ar.model.ArRotation
import com.example.roomstyler.ar.model.ArScene
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArSceneManager @Inject constructor() {
    
    private val _currentScene = MutableStateFlow(
        ArScene(
            id = UUID.randomUUID().toString(),
            name = "New Scene"
        )
    )
    val currentScene: StateFlow<ArScene> = _currentScene.asStateFlow()
    
    private val _selectedObjectId = MutableStateFlow<String?>(null)
    val selectedObjectId: StateFlow<String?> = _selectedObjectId.asStateFlow()
    
    fun addObject(modelPath: String, position: ArPosition): String {
        val newObject = ArObject(
            id = UUID.randomUUID().toString(),
            modelPath = modelPath,
            position = position
        )
        
        val updatedScene = _currentScene.value.copy(
            objects = _currentScene.value.objects + newObject,
            lastModified = System.currentTimeMillis()
        )
        
        _currentScene.value = updatedScene
        return newObject.id
    }
    
    fun selectObject(objectId: String?) {
        _selectedObjectId.value = objectId
    }
    
    fun moveObject(objectId: String, newPosition: ArPosition) {
        val currentObjects = _currentScene.value.objects
        val updatedObjects = currentObjects.map { obj ->
            if (obj.id == objectId) {
                obj.copy(position = newPosition)
            } else {
                obj
            }
        }
        
        _currentScene.value = _currentScene.value.copy(
            objects = updatedObjects,
            lastModified = System.currentTimeMillis()
        )
    }
    
    fun rotateObject(objectId: String, newRotation: ArRotation) {
        val currentObjects = _currentScene.value.objects
        val updatedObjects = currentObjects.map { obj ->
            if (obj.id == objectId) {
                obj.copy(rotation = newRotation)
            } else {
                obj
            }
        }
        
        _currentScene.value = _currentScene.value.copy(
            objects = updatedObjects,
            lastModified = System.currentTimeMillis()
        )
    }
    
    fun deleteObject(objectId: String) {
        val updatedObjects = _currentScene.value.objects.filter { it.id != objectId }
        
        _currentScene.value = _currentScene.value.copy(
            objects = updatedObjects,
            lastModified = System.currentTimeMillis()
        )
        
        // Clear selection if deleted object was selected
        if (_selectedObjectId.value == objectId) {
            _selectedObjectId.value = null
        }
    }
    
    fun clearScene() {
        _currentScene.value = ArScene(
            id = UUID.randomUUID().toString(),
            name = "New Scene"
        )
        _selectedObjectId.value = null
    }
    
    fun loadScene(scene: ArScene) {
        _currentScene.value = scene
        _selectedObjectId.value = null
    }
}
