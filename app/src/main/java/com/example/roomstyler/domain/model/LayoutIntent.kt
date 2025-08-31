package com.example.roomstyler.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LayoutIntent(
    val action: String, // "add", "move", "remove", "replace", "generate_options"
    val target: String? = null,
    val size_cm: Size3D? = null,
    val constraints: List<String> = emptyList(),
    val position_hint: String? = null,
    val count: Int = 3
)
