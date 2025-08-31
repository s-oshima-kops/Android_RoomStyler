package com.example.roomstyler.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: String = "default-room",
    val name: String = "部屋",
    val widthM: Double,
    val depthM: Double,
    val heightM: Double,
    val windows: List<String> = emptyList(), // 簡易表現（MVP）
    val doors: List<String> = emptyList(),
    val powerOutlets: List<String> = emptyList()
) {
    // Size3D形式での寸法取得
    val dimensions: Size3D
        get() = Size3D(
            w = widthM,
            d = depthM,
            h = heightM
        )
}

@Serializable
data class Door(
    val id: String,
    val position: Pose3D,
    val dimensions: Size3D
)

@Serializable
data class Window(
    val id: String,
    val position: Pose3D,
    val dimensions: Size3D
)
