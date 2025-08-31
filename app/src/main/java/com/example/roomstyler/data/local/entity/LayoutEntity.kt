package com.example.roomstyler.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "layout")
data class LayoutEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val roomWidthM: Double,
    val roomDepthM: Double,
    val roomHeightM: Double,
    val windowsJson: String = "[]", // JSON array of window positions
    val doorsJson: String = "[]", // JSON array of door positions  
    val powerOutletsJson: String = "[]", // JSON array of outlet positions
    val furnituresJson: String = "[]", // JSON array of placed furniture with positions
    val score: Double,
    val reasonsJson: String = "[]", // JSON array of scoring reasons
    val isSnapshot: Boolean = false, // true for AR snapshots
    val snapshotImagePath: String? = null, // path to snapshot image
    val arSceneJson: String? = null, // AR scene data for restoration
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
