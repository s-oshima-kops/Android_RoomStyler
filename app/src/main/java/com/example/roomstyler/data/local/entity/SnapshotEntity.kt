package com.example.roomstyler.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snapshot")
data class SnapshotEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val layoutId: String, // Reference to layout
    val imagePath: String, // Path to captured AR image
    val thumbnailPath: String? = null, // Path to thumbnail image
    val arSceneJson: String, // Complete AR scene data
    val furnitureCount: Int = 0,
    val roomDimensions: String, // "width x depth x height"
    val tags: String = "[]", // JSON array of tags
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
