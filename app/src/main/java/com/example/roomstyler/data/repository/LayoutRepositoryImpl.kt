package com.example.roomstyler.data.repository

import com.example.roomstyler.data.local.dao.LayoutDao
import com.example.roomstyler.data.local.dao.FurnitureDao
import com.example.roomstyler.data.mapper.toEntity
import com.example.roomstyler.data.mapper.toModel
import com.example.roomstyler.domain.model.Layout
import com.example.roomstyler.domain.repository.LayoutRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LayoutRepositoryImpl @Inject constructor(
    private val layoutDao: LayoutDao,
    private val furnitureDao: FurnitureDao
) : LayoutRepository {

    private val gson = Gson()

    override fun getAllLayouts(): Flow<List<Layout>> {
        return layoutDao.getAllLayouts().map { entities ->
            entities.map { entity ->
                val furnitures = getFurnituresFromJson(entity.furnituresJson)
                entity.toModel(furnitures)
            }
        }
    }

    override suspend fun getLayoutById(id: String): Layout? {
        val entity = layoutDao.getLayoutById(id) ?: return null
        val furnitures = getFurnituresFromJson(entity.furnituresJson)
        return entity.toModel(furnitures)
    }

    override fun getRecentLayouts(limit: Int): Flow<List<Layout>> {
        return layoutDao.getRecentLayouts(limit).map { entities ->
            entities.map { entity ->
                val furnitures = getFurnituresFromJson(entity.furnituresJson)
                entity.toModel(furnitures)
            }
        }
    }

    override suspend fun insertLayout(layout: Layout) {
        layoutDao.insertLayout(layout.toEntity())
    }

    override suspend fun insertLayouts(layouts: List<Layout>) {
        layoutDao.insertLayouts(layouts.map { it.toEntity() })
    }

    override suspend fun updateLayout(layout: Layout) {
        layoutDao.updateLayout(layout.toEntity())
    }

    override suspend fun deleteLayout(layout: Layout) {
        layoutDao.deleteLayout(layout.toEntity())
    }

    override suspend fun deleteLayoutById(id: String) {
        layoutDao.deleteLayoutById(id)
    }

    private suspend fun getFurnituresFromJson(furnituresJson: String): List<com.example.roomstyler.domain.model.Furniture> {
        return try {
            val furnitureType = object : TypeToken<List<com.example.roomstyler.domain.model.Furniture>>() {}.type
            gson.fromJson<List<com.example.roomstyler.domain.model.Furniture>>(furnituresJson, furnitureType) ?: emptyList()
        } catch (e: Exception) {
            // Fallback: try to parse as list of IDs and fetch from database
            try {
                val idListType = object : TypeToken<List<String>>() {}.type
                val furnitureIds = gson.fromJson<List<String>>(furnituresJson, idListType) ?: emptyList()
                furnitureIds.mapNotNull { id ->
                    furnitureDao.getFurnitureById(id)?.toModel()
                }
            } catch (e2: Exception) {
                emptyList()
            }
        }
    }
}
