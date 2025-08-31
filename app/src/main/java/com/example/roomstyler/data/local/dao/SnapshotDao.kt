package com.example.roomstyler.data.local.dao

import androidx.room.*
import com.example.roomstyler.data.local.entity.SnapshotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SnapshotDao {
    @Query("SELECT * FROM snapshot ORDER BY createdAt DESC")
    fun getAllSnapshots(): Flow<List<SnapshotEntity>>

    @Query("SELECT * FROM snapshot WHERE id = :id")
    suspend fun getSnapshotById(id: String): SnapshotEntity?

    @Query("SELECT * FROM snapshot WHERE layoutId = :layoutId ORDER BY createdAt DESC")
    fun getSnapshotsByLayout(layoutId: String): Flow<List<SnapshotEntity>>

    @Query("SELECT * FROM snapshot WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteSnapshots(): Flow<List<SnapshotEntity>>

    @Query("SELECT * FROM snapshot ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentSnapshots(limit: Int = 10): Flow<List<SnapshotEntity>>

    @Query("SELECT * FROM snapshot WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchSnapshots(query: String): Flow<List<SnapshotEntity>>

    @Query("SELECT COUNT(*) FROM snapshot")
    suspend fun getSnapshotCount(): Int

    @Query("SELECT COUNT(*) FROM snapshot WHERE isFavorite = 1")
    suspend fun getFavoriteCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: SnapshotEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshots(snapshots: List<SnapshotEntity>)

    @Update
    suspend fun updateSnapshot(snapshot: SnapshotEntity)

    @Delete
    suspend fun deleteSnapshot(snapshot: SnapshotEntity)

    @Query("DELETE FROM snapshot WHERE id = :id")
    suspend fun deleteSnapshotById(id: String)

    @Query("UPDATE snapshot SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)
}
