package com.example.roomstyler.ar.model

import kotlinx.serialization.Serializable

@Serializable
data class ArScene(
    val id: String,
    val name: String,
    val objects: List<ArObject> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis()
)

@Serializable
data class ArPlane(
    val id: String,
    val type: PlaneType,
    val centerPose: ArPosition,
    val extentX: Float,
    val extentZ: Float
)

enum class PlaneType {
    HORIZONTAL_UPWARD_FACING,
    HORIZONTAL_DOWNWARD_FACING,
    VERTICAL
}
