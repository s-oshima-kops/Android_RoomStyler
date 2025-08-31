package com.example.roomstyler.data.repository

import com.example.roomstyler.data.local.dao.FurnitureDao
import com.example.roomstyler.data.mapper.toEntity
import com.example.roomstyler.data.mapper.toModel
import com.example.roomstyler.domain.model.Furniture
import com.example.roomstyler.domain.repository.FurnitureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FurnitureRepositoryImpl @Inject constructor(
    private val furnitureDao: FurnitureDao
) : FurnitureRepository {

    override fun getAllFurniture(): Flow<List<Furniture>> {
        return furnitureDao.getAllFurniture().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getFurnitureById(id: String): Furniture? {
        return furnitureDao.getFurnitureById(id)?.toModel()
    }

    override fun getFurnitureByCategory(category: String): Flow<List<Furniture>> {
        return furnitureDao.getFurnitureByCategory(category).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun insertFurniture(furniture: Furniture) {
        furnitureDao.insertFurniture(furniture.toEntity())
    }

    override suspend fun insertFurnitures(furnitures: List<Furniture>) {
        furnitureDao.insertFurnitures(furnitures.map { it.toEntity() })
    }

    override suspend fun updateFurniture(furniture: Furniture) {
        furnitureDao.updateFurniture(furniture.toEntity())
    }

    override suspend fun deleteFurniture(furniture: Furniture) {
        furnitureDao.deleteFurniture(furniture.toEntity())
    }

    override suspend fun deleteFurnitureById(id: String) {
        furnitureDao.deleteFurnitureById(id)
    }
}
