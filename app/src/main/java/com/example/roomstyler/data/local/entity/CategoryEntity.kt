package com.example.roomstyler.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val nameEn: String,
    val description: String? = null,
    val iconPath: String? = null,
    val color: String? = null, // Hex color code
    val sortOrder: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
