package com.example.roomstyler.data.repository

import com.example.roomstyler.data.local.dao.SnapshotDao
import com.example.roomstyler.data.mapper.SnapshotModel
import com.example.roomstyler.data.mapper.toEntity
import com.example.roomstyler.data.mapper.toModel
import com.example.roomstyler.domain.repository.SnapshotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnapshotRepositoryImpl @Inject constructor(
    private val snapshotDao: SnapshotDao
) : SnapshotRepository {

    override fun getAllSnapshots(): Flow<List<SnapshotModel>> {
        return snapshotDao.getAllSnapshots().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getSnapshotById(id: String): SnapshotModel? {
        return snapshotDao.getSnapshotById(id)?.toModel()
    }

    override fun getSnapshotsByLayout(layoutId: String): Flow<List<SnapshotModel>> {
        return snapshotDao.getSnapshotsByLayout(layoutId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getFavoriteSnapshots(): Flow<List<SnapshotModel>> {
        return snapshotDao.getFavoriteSnapshots().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getRecentSnapshots(limit: Int): Flow<List<SnapshotModel>> {
        return snapshotDao.getRecentSnapshots(limit).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun searchSnapshots(query: String): Flow<List<SnapshotModel>> {
        return snapshotDao.searchSnapshots(query).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getSnapshotCount(): Int {
        return snapshotDao.getSnapshotCount()
    }

    override suspend fun getFavoriteCount(): Int {
        return snapshotDao.getFavoriteCount()
    }

    override suspend fun insertSnapshot(snapshot: SnapshotModel) {
        snapshotDao.insertSnapshot(snapshot.toEntity())
    }

    override suspend fun insertSnapshots(snapshots: List<SnapshotModel>) {
        snapshotDao.insertSnapshots(snapshots.map { it.toEntity() })
    }

    override suspend fun updateSnapshot(snapshot: SnapshotModel) {
        snapshotDao.updateSnapshot(snapshot.toEntity())
    }

    override suspend fun deleteSnapshot(snapshot: SnapshotModel) {
        snapshotDao.deleteSnapshot(snapshot.toEntity())
    }

    override suspend fun deleteSnapshotById(id: String) {
        snapshotDao.deleteSnapshotById(id)
    }

    override suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
        snapshotDao.updateFavoriteStatus(id, isFavorite)
    }
}
