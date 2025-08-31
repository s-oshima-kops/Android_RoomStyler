package com.example.roomstyler.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "furniture")
data class FurnitureEntity(
    @PrimaryKey
    val id: String,
    val category: String, // DESK, CHAIR, SOFA, SHELF, BED, TABLE, TV, STORAGE, ETC
    val name: String,
    val description: String? = null,
    val sizeW: Double, // width in cm
    val sizeD: Double, // depth in cm
    val sizeH: Double, // height in cm
    val positionX: Double? = null,
    val positionY: Double? = null,
    val positionZ: Double? = null,
    val yaw: Double? = null,
    val pitch: Double? = null,
    val roll: Double? = null,
    val material: String? = null,
    val color: String? = null,
    val price: Double? = null,
    val brand: String? = null,
    val modelPath: String? = null, // glTF model path
    val thumbnailUrl: String? = null,
    val isCustom: Boolean = false, // true for user-created furniture
    val metaJson: String = "{}", // JSON string for meta map
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
