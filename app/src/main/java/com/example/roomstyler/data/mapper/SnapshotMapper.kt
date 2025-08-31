package com.example.roomstyler.data.mapper

import com.example.roomstyler.ar.model.ArScene
import com.example.roomstyler.data.local.entity.SnapshotEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val gson = Gson()

data class SnapshotModel(
    val id: String,
    val name: String,
    val description: String? = null,
    val layoutId: String,
    val imagePath: String,
    val thumbnailPath: String? = null,
    val arScene: ArScene,
    val furnitureCount: Int = 0,
    val roomDimensions: String,
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

fun SnapshotEntity.toModel(): SnapshotModel {
    val arScene = try {
        gson.fromJson(arSceneJson, ArScene::class.java)
    } catch (e: Exception) {
        ArScene(id = "error", name = "Error Scene")
    }
    
    val tagsList = try {
        val listType = object : TypeToken<List<String>>() {}.type
        gson.fromJson<List<String>>(tags, listType) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    return SnapshotModel(
        id = id,
        name = name,
        description = description,
        layoutId = layoutId,
        imagePath = imagePath,
        thumbnailPath = thumbnailPath,
        arScene = arScene,
        furnitureCount = furnitureCount,
        roomDimensions = roomDimensions,
        tags = tagsList,
        isFavorite = isFavorite,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun SnapshotModel.toEntity(): SnapshotEntity {
    return SnapshotEntity(
        id = id,
        name = name,
        description = description,
        layoutId = layoutId,
        imagePath = imagePath,
        thumbnailPath = thumbnailPath,
        arSceneJson = gson.toJson(arScene),
        furnitureCount = furnitureCount,
        roomDimensions = roomDimensions,
        tags = gson.toJson(tags),
        isFavorite = isFavorite,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
