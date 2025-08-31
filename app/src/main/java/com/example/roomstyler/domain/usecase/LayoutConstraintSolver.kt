package com.example.roomstyler.domain.usecase

import com.example.roomstyler.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*
import kotlin.random.Random

@Singleton
class LayoutConstraintSolver @Inject constructor() {

    private val constraints = LayoutConstraint()
    private val random = Random.Default

    /**
     * レイアウト案を生成する
     */
    suspend fun generateLayoutProposals(
        room: Room,
        availableFurniture: List<Furniture>,
        layoutIntent: LayoutIntent? = null,
        proposalCount: Int = 5
    ): List<LayoutProposal> = withContext(Dispatchers.Default) {
        
        val proposals = mutableListOf<LayoutProposal>()
        
        repeat(proposalCount) { index ->
            val furniture = generateRandomLayout(room, availableFurniture, layoutIntent)
            val optimizedFurniture = optimizeLayout(room, furniture)
            val score = calculateLayoutScore(room, optimizedFurniture)
            
            proposals.add(
                LayoutProposal(
                    id = UUID.randomUUID().toString(),
                    name = "レイアウト案 ${index + 1}",
                    furniture = optimizedFurniture,
                    score = score
                )
            )
        }
        
        // スコア順でソート（降順）
        proposals.sortedByDescending { it.score.totalScore }
    }

    /**
     * ランダムな初期レイアウトを生成
     */
    private fun generateRandomLayout(
        room: Room,
        availableFurniture: List<Furniture>,
        layoutIntent: LayoutIntent?
    ): List<Furniture> {
        val placedFurniture = mutableListOf<Furniture>()
        
        // 必要な家具を選択（最大8個）
        val selectedFurniture = availableFurniture.shuffled().take(8)
        
        selectedFurniture.forEach { furniture ->
            val position = generateRandomPosition(room, furniture, placedFurniture, layoutIntent)
            val rotation = random.nextFloat() * 360f
            
            placedFurniture.add(
                furniture.copy(
                    position = Pose3D(
                        x = position.w,
                        y = position.h,
                        z = position.d,
                        yaw = rotation.toDouble(),
                        pitch = 0.0,
                        roll = 0.0
                    )
                )
            )
        }
        
        return placedFurniture
    }

    /**
     * ランダムな位置を生成（制約を考慮）
     */
    private fun generateRandomPosition(
        room: Room,
        furniture: Furniture,
        existingFurniture: List<Furniture>,
        layoutIntent: LayoutIntent?
    ): Size3D {
        val maxAttempts = 50
        repeat(maxAttempts) {
            val x = random.nextDouble() * (room.dimensions.w - furniture.sizeCm.w) + 
                    furniture.sizeCm.w / 2
            val z = random.nextDouble() * (room.dimensions.d - furniture.sizeCm.d) + 
                    furniture.sizeCm.d / 2
            val y = 0.0 // 床面
            
            val position = Size3D(x, y, z)
            
            // ヒントがある場合は優先
            val hintPosition = applyPositionHint(room, furniture, layoutIntent)
            if (hintPosition != null) {
                return hintPosition
            }
            
            // 他の家具との重複チェック
            if (!isOverlapping(furniture.copy(position = Pose3D(x = position.w, y = position.h, z = position.d, yaw = 0.0, pitch = 0.0, roll = 0.0)), existingFurniture)) {
                return position
            }
        }
        
        // フォールバック: 部屋の中央
        return Size3D(room.dimensions.w / 2, 0.0, room.dimensions.d / 2)
    }

    /**
     * 位置ヒントを適用
     */
    private fun applyPositionHint(
        room: Room,
        furniture: Furniture,
        layoutIntent: LayoutIntent?
    ): Size3D? {
        val hint = layoutIntent?.position_hint ?: return null
        
        return when (hint) {
            "near_window" -> Size3D(
                room.dimensions.w * 0.8,
                0.0,
                room.dimensions.d * 0.2
            )
            "near_wall_north" -> Size3D(
                room.dimensions.w / 2,
                0.0,
                furniture.sizeCm.d / 2 + constraints.wallClearance
            )
            "near_wall_south" -> Size3D(
                room.dimensions.w / 2,
                0.0,
                room.dimensions.d - furniture.sizeCm.d / 2 - constraints.wallClearance
            )
            "near_wall_east" -> Size3D(
                room.dimensions.w - furniture.sizeCm.w / 2 - constraints.wallClearance,
                0.0,
                room.dimensions.d / 2
            )
            "near_wall_west" -> Size3D(
                furniture.sizeCm.w / 2 + constraints.wallClearance,
                0.0,
                room.dimensions.d / 2
            )
            "center" -> Size3D(
                room.dimensions.w / 2,
                0.0,
                room.dimensions.d / 2
            )
            "corner" -> Size3D(
                furniture.sizeCm.w / 2 + constraints.wallClearance,
                0.0,
                furniture.sizeCm.d / 2 + constraints.wallClearance
            )
            else -> null
        }
    }

    /**
     * 局所探索でレイアウトを最適化
     */
    private fun optimizeLayout(room: Room, furniture: List<Furniture>): List<Furniture> {
        var currentFurniture = furniture.toMutableList()
        var currentScore = calculateLayoutScore(room, currentFurniture).totalScore
        
        val maxIterations = 100
        repeat(maxIterations) {
            val candidate = perturbLayout(room, currentFurniture)
            val candidateScore = calculateLayoutScore(room, candidate).totalScore
            
            if (candidateScore > currentScore) {
                currentFurniture = candidate.toMutableList()
                currentScore = candidateScore
            }
        }
        
        return currentFurniture
    }

    /**
     * レイアウトを微調整
     */
    private fun perturbLayout(room: Room, furniture: List<Furniture>): List<Furniture> {
        val result = furniture.toMutableList()
        
        if (result.isNotEmpty()) {
            val index = random.nextInt(result.size)
            val item = result[index]
            
            // 位置を微調整
            val deltaX = (random.nextDouble() - 0.5) * 0.5
            val deltaZ = (random.nextDouble() - 0.5) * 0.5
            val deltaRotation = (random.nextDouble() - 0.5) * 30.0
            
            val currentPos = item.position ?: Pose3D(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
            
            val newX = (currentPos.x + deltaX).coerceIn(
                item.sizeCm.w / 2,
                room.dimensions.w - item.sizeCm.w / 2
            )
            val newZ = (currentPos.z + deltaZ).coerceIn(
                item.sizeCm.d / 2,
                room.dimensions.d - item.sizeCm.d / 2
            )
            val newYaw = (currentPos.yaw + deltaRotation) % 360.0
            
            result[index] = item.copy(
                position = Pose3D(
                    x = newX,
                    y = 0.0,
                    z = newZ,
                    yaw = newYaw,
                    pitch = 0.0,
                    roll = 0.0
                )
            )
        }
        
        return result
    }

    /**
     * レイアウトスコアを計算
     */
    private fun calculateLayoutScore(room: Room, furniture: List<Furniture>): LayoutScore {
        val reasons = mutableListOf<String>()
        
        val corridorScore = calculateCorridorScore(room, furniture, reasons)
        val tvDistanceScore = calculateTvDistanceScore(furniture, reasons)
        val doorAvoidanceScore = calculateDoorAvoidanceScore(room, furniture, reasons)
        val windowAvoidanceScore = calculateWindowAvoidanceScore(room, furniture, reasons)
        val hintComplianceScore = 1.0f // 簡易実装
        
        val totalScore = (corridorScore + tvDistanceScore + doorAvoidanceScore + 
                         windowAvoidanceScore + hintComplianceScore) / 5f
        
        return LayoutScore(
            totalScore = totalScore,
            corridorScore = corridorScore,
            tvDistanceScore = tvDistanceScore,
            doorAvoidanceScore = doorAvoidanceScore,
            windowAvoidanceScore = windowAvoidanceScore,
            hintComplianceScore = hintComplianceScore,
            reasons = reasons
        )
    }

    /**
     * 通路幅スコアを計算
     */
    private fun calculateCorridorScore(
        @Suppress("UNUSED_PARAMETER") room: Room,
        furniture: List<Furniture>,
        reasons: MutableList<String>
    ): Float {
        // 簡易実装: 家具間の最小距離をチェック
        var minDistance = Float.MAX_VALUE
        
        for (i in furniture.indices) {
            for (j in i + 1 until furniture.size) {
                val distance = calculateDistance(furniture[i], furniture[j])
                minDistance = min(minDistance, distance)
            }
        }
        
        return if (minDistance >= constraints.minCorridorWidth) {
            reasons.add("通路幅が確保されています (${String.format("%.1f", minDistance)}m)")
            1.0f
        } else {
            reasons.add("通路幅が不足しています (${String.format("%.1f", minDistance)}m < ${constraints.minCorridorWidth}m)")
            max(0f, minDistance / constraints.minCorridorWidth)
        }
    }

    /**
     * TV視距離スコアを計算
     */
    private fun calculateTvDistanceScore(
        furniture: List<Furniture>,
        reasons: MutableList<String>
    ): Float {
        val tv = furniture.find { it.category == "electronics" }
        val sofa = furniture.find { it.category == "seating" }
        
        if (tv == null || sofa == null) {
            reasons.add("TVまたはソファが配置されていません")
            return 0.5f
        }
        
        val distance = calculateDistance(tv, sofa)
        return if (distance >= constraints.minTvDistance) {
            reasons.add("TV視聴距離が適切です (${String.format("%.1f", distance)}m)")
            1.0f
        } else {
            reasons.add("TV視聴距離が近すぎます (${String.format("%.1f", distance)}m < ${constraints.minTvDistance}m)")
            max(0f, distance / constraints.minTvDistance)
        }
    }

    /**
     * 扉回避スコアを計算
     */
    private fun calculateDoorAvoidanceScore(
        @Suppress("UNUSED_PARAMETER") room: Room,
        furniture: List<Furniture>,
        reasons: MutableList<String>
    ): Float {
        // 簡易実装: 扉は部屋の入口（0, 0）付近と仮定
        val doorPosition = Pose3D(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        var totalScore = 0f
        var count = 0
        
        furniture.forEach { item ->
            val itemPos = item.position ?: Pose3D(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
            val distance = sqrt(
                (itemPos.x - doorPosition.x).pow(2) +
                (itemPos.z - doorPosition.z).pow(2)
            ).toFloat()
            
            if (distance < constraints.doorAvoidanceRadius) {
                reasons.add("${item.id}が扉に近すぎます")
                totalScore += kotlin.math.max(0f, distance / constraints.doorAvoidanceRadius)
            } else {
                totalScore += 1.0f
            }
            count++
        }
        
        return if (count > 0) totalScore / count else 1.0f
    }

    /**
     * 窓回避スコアを計算
     */
    private fun calculateWindowAvoidanceScore(
        room: Room,
        furniture: List<Furniture>,
        reasons: MutableList<String>
    ): Float {
        // 簡易実装: 窓は部屋の右側の壁と仮定
        val windowPosition = Pose3D(room.dimensions.w, 0.0, room.dimensions.d / 2, 0.0, 0.0, 0.0)
        var totalScore = 0f
        var count = 0
        
        furniture.forEach { item ->
            val itemPos = item.position ?: Pose3D(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
            val distance = sqrt(
                (itemPos.x - windowPosition.x).pow(2) +
                (itemPos.z - windowPosition.z).pow(2)
            ).toFloat()
            
            if (distance < constraints.windowAvoidanceRadius) {
                reasons.add("${item.id}が窓に近すぎます")
                totalScore += kotlin.math.max(0f, distance / constraints.windowAvoidanceRadius)
            } else {
                totalScore += 1.0f
            }
            count++
        }
        
        return if (count > 0) totalScore / count else 1.0f
    }

    /**
     * 家具間の距離を計算
     */
    private fun calculateDistance(furniture1: Furniture, furniture2: Furniture): Float {
        val pos1 = furniture1.position ?: Pose3D(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        val pos2 = furniture2.position ?: Pose3D(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        return sqrt(
            (pos1.x - pos2.x).pow(2) +
            (pos1.z - pos2.z).pow(2)
        ).toFloat()
    }

    /**
     * 家具の重複チェック
     */
    private fun isOverlapping(furniture: Furniture, existingFurniture: List<Furniture>): Boolean {
        return existingFurniture.any { existing ->
            val distance = calculateDistance(furniture, existing)
            val minDistance = ((furniture.sizeCm.w + existing.sizeCm.w) / 2 +
                            (furniture.sizeCm.d + existing.sizeCm.d) / 2).toFloat()
            distance < minDistance * 0.8f // 少し余裕を持たせる
        }
    }
}
