package com.example.roomstyler.ar.model

import kotlinx.serialization.Serializable

@Serializable
data class ArObject(
    val id: String,
    val modelPath: String,
    val position: ArPosition,
    val rotation: ArRotation = ArRotation(),
    val scale: ArScale = ArScale(),
    val anchorId: String? = null
)

@Serializable
data class ArPosition(
    val x: Float,
    val y: Float,
    val z: Float
)

@Serializable
data class ArRotation(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val w: Float = 1f // quaternion w component
)

@Serializable
data class ArScale(
    val x: Float = 1f,
    val y: Float = 1f,
    val z: Float = 1f
)
