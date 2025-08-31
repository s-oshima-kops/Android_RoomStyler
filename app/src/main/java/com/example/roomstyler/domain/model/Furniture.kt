package com.example.roomstyler.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Furniture(
    val id: String,
    val category: String, // FurnitureCategory as String for serialization
    val sizeCm: Size3D,
    val position: Pose3D? = null,
    val material: String? = null,
    val color: String? = null,
    val meta: Map<String, String> = emptyMap()
)
