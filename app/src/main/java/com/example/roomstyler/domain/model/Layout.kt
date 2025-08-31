package com.example.roomstyler.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Layout(
    val id: String,
    val room: Room,
    val furnitures: List<Furniture>,
    val score: Double,
    val reasons: List<String>
)
