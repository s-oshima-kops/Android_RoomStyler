package com.example.roomstyler.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Size3D(
    val w: Double,
    val d: Double,
    val h: Double
)
