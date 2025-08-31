package com.example.roomstyler.domain.repository

import com.example.roomstyler.data.mapper.SnapshotModel
import kotlinx.coroutines.flow.Flow

interface SnapshotRepository {
    fun getAllSnapshots(): Flow<List<SnapshotModel>>
    suspend fun getSnapshotById(id: String): SnapshotModel?
    fun getSnapshotsByLayout(layoutId: String): Flow<List<SnapshotModel>>
    fun getFavoriteSnapshots(): Flow<List<SnapshotModel>>
    fun getRecentSnapshots(limit: Int = 10): Flow<List<SnapshotModel>>
    fun searchSnapshots(query: String): Flow<List<SnapshotModel>>
    suspend fun getSnapshotCount(): Int
    suspend fun getFavoriteCount(): Int
    suspend fun insertSnapshot(snapshot: SnapshotModel)
    suspend fun insertSnapshots(snapshots: List<SnapshotModel>)
    suspend fun updateSnapshot(snapshot: SnapshotModel)
    suspend fun deleteSnapshot(snapshot: SnapshotModel)
    suspend fun deleteSnapshotById(id: String)
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)
}
