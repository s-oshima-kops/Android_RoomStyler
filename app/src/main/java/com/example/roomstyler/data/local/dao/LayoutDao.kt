package com.example.roomstyler.data.local.dao

import androidx.room.*
import com.example.roomstyler.data.local.entity.LayoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LayoutDao {
    @Query("SELECT * FROM layout ORDER BY updatedAt DESC")
    fun getAllLayouts(): Flow<List<LayoutEntity>>

    @Query("SELECT * FROM layout WHERE id = :id")
    suspend fun getLayoutById(id: String): LayoutEntity?

    @Query("SELECT * FROM layout WHERE isSnapshot = 0 ORDER BY updatedAt DESC LIMIT :limit")
    fun getRecentLayouts(limit: Int = 10): Flow<List<LayoutEntity>>

    @Query("SELECT * FROM layout WHERE isSnapshot = 1 ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentSnapshots(limit: Int = 10): Flow<List<LayoutEntity>>

    @Query("SELECT * FROM layout WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchLayouts(query: String): Flow<List<LayoutEntity>>

    @Query("SELECT * FROM layout WHERE score >= :minScore ORDER BY score DESC")
    fun getLayoutsByScore(minScore: Double): Flow<List<LayoutEntity>>

    @Query("SELECT COUNT(*) FROM layout WHERE isSnapshot = 1")
    suspend fun getSnapshotCount(): Int

    @Query("SELECT COUNT(*) FROM layout WHERE isSnapshot = 0")
    suspend fun getLayoutCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLayout(layout: LayoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLayouts(layouts: List<LayoutEntity>)

    @Update
    suspend fun updateLayout(layout: LayoutEntity)

    @Delete
    suspend fun deleteLayout(layout: LayoutEntity)

    @Query("DELETE FROM layout WHERE id = :id")
    suspend fun deleteLayoutById(id: String)

    @Query("DELETE FROM layout WHERE isSnapshot = 1")
    suspend fun deleteAllSnapshots()
}
