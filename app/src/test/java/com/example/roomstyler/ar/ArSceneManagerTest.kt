package com.example.roomstyler.ar

import com.example.roomstyler.ar.model.ArPosition
import com.example.roomstyler.ar.model.ArRotation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ArSceneManagerTest {

    private lateinit var arSceneManager: ArSceneManager

    @Before
    fun setUp() {
        arSceneManager = ArSceneManager()
    }

    @Test
    fun `addObject adds object to scene`() = runTest {
        // Given
        val position = ArPosition(1.0f, 0.0f, -1.0f)
        val modelPath = "test_model.glb"

        // When
        val objectId = arSceneManager.addObject(modelPath, position)

        // Then
        val currentScene = arSceneManager.currentScene.value
        assertEquals(1, currentScene.objects.size)
        
        val addedObject = currentScene.objects.first()
        assertEquals(objectId, addedObject.id)
        assertEquals(modelPath, addedObject.modelPath)
        assertEquals(position, addedObject.position)
    }

    @Test
    fun `selectObject updates selected object id`() = runTest {
        // Given
        val position = ArPosition(0.0f, 0.0f, 0.0f)
        val objectId = arSceneManager.addObject("test.glb", position)

        // When
        arSceneManager.selectObject(objectId)

        // Then
        assertEquals(objectId, arSceneManager.selectedObjectId.value)
    }

    @Test
    fun `moveObject updates object position`() = runTest {
        // Given
        val initialPosition = ArPosition(0.0f, 0.0f, 0.0f)
        val objectId = arSceneManager.addObject("test.glb", initialPosition)
        val newPosition = ArPosition(1.0f, 1.0f, 1.0f)

        // When
        arSceneManager.moveObject(objectId, newPosition)

        // Then
        val currentScene = arSceneManager.currentScene.value
        val movedObject = currentScene.objects.first { it.id == objectId }
        assertEquals(newPosition, movedObject.position)
    }

    @Test
    fun `rotateObject updates object rotation`() = runTest {
        // Given
        val position = ArPosition(0.0f, 0.0f, 0.0f)
        val objectId = arSceneManager.addObject("test.glb", position)
        val newRotation = ArRotation(0.0f, 90.0f, 0.0f, 1.0f)

        // When
        arSceneManager.rotateObject(objectId, newRotation)

        // Then
        val currentScene = arSceneManager.currentScene.value
        val rotatedObject = currentScene.objects.first { it.id == objectId }
        assertEquals(newRotation, rotatedObject.rotation)
    }

    @Test
    fun `deleteObject removes object from scene`() = runTest {
        // Given
        val position = ArPosition(0.0f, 0.0f, 0.0f)
        val objectId = arSceneManager.addObject("test.glb", position)
        arSceneManager.selectObject(objectId)

        // When
        arSceneManager.deleteObject(objectId)

        // Then
        val currentScene = arSceneManager.currentScene.value
        assertEquals(0, currentScene.objects.size)
        assertNull(arSceneManager.selectedObjectId.value)
    }

    @Test
    fun `clearScene removes all objects and clears selection`() = runTest {
        // Given
        arSceneManager.addObject("test1.glb", ArPosition(0.0f, 0.0f, 0.0f))
        val objectId2 = arSceneManager.addObject("test2.glb", ArPosition(1.0f, 0.0f, 0.0f))
        arSceneManager.selectObject(objectId2)

        // When
        arSceneManager.clearScene()

        // Then
        val currentScene = arSceneManager.currentScene.value
        assertEquals(0, currentScene.objects.size)
        assertNull(arSceneManager.selectedObjectId.value)
    }
}
