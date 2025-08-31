package com.example.roomstyler.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Pose3D(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Double,
    val pitch: Double,
    val roll: Double
)
