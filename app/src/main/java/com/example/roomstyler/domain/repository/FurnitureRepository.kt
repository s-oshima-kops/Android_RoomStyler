package com.example.roomstyler.domain.repository

import com.example.roomstyler.domain.model.Furniture
import kotlinx.coroutines.flow.Flow

interface FurnitureRepository {
    fun getAllFurniture(): Flow<List<Furniture>>
    suspend fun getFurnitureById(id: String): Furniture?
    fun getFurnitureByCategory(category: String): Flow<List<Furniture>>
    suspend fun insertFurniture(furniture: Furniture)
    suspend fun insertFurnitures(furnitures: List<Furniture>)
    suspend fun updateFurniture(furniture: Furniture)
    suspend fun deleteFurniture(furniture: Furniture)
    suspend fun deleteFurnitureById(id: String)
}
