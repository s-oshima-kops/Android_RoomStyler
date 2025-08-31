package com.example.roomstyler.domain.model

import kotlinx.serialization.Serializable

/**
 * レイアウト制約の定義
 */
@Serializable
data class LayoutConstraint(
    val minCorridorWidth: Float = 0.6f, // 最小通路幅（メートル）
    val minTvDistance: Float = 2.0f, // TV視聴距離（メートル）
    val doorAvoidanceRadius: Float = 0.8f, // 扉回避半径（メートル）
    val windowAvoidanceRadius: Float = 0.5f, // 窓回避半径（メートル）
    val wallClearance: Float = 0.1f // 壁からの最小距離（メートル）
)

/**
 * レイアウトスコアの詳細
 */
@Serializable
data class LayoutScore(
    val totalScore: Float,
    val corridorScore: Float,
    val tvDistanceScore: Float,
    val doorAvoidanceScore: Float,
    val windowAvoidanceScore: Float,
    val hintComplianceScore: Float,
    val reasons: List<String>
) {
    companion object {
        fun empty() = LayoutScore(
            totalScore = 0f,
            corridorScore = 0f,
            tvDistanceScore = 0f,
            doorAvoidanceScore = 0f,
            windowAvoidanceScore = 0f,
            hintComplianceScore = 0f,
            reasons = emptyList()
        )
    }
}

/**
 * レイアウト案
 */
@Serializable
data class LayoutProposal(
    val id: String,
    val name: String,
    val furniture: List<Furniture>,
    val score: LayoutScore,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 制約違反の詳細
 */
@Serializable
data class ConstraintViolation(
    val type: ViolationType,
    val severity: Severity,
    val description: String,
    val furnitureIds: List<String> = emptyList()
)

@Serializable
enum class ViolationType {
    CORRIDOR_WIDTH,
    TV_DISTANCE,
    DOOR_BLOCKAGE,
    WINDOW_BLOCKAGE,
    WALL_COLLISION,
    FURNITURE_OVERLAP
}

@Serializable
enum class Severity {
    LOW, MEDIUM, HIGH, CRITICAL
}
