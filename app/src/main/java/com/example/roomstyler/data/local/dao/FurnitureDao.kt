package com.example.roomstyler.data.local.dao

import androidx.room.*
import com.example.roomstyler.data.local.entity.FurnitureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FurnitureDao {
    @Query("SELECT * FROM furniture ORDER BY category, name")
    fun getAllFurniture(): Flow<List<FurnitureEntity>>

    @Query("SELECT * FROM furniture WHERE id = :id")
    suspend fun getFurnitureById(id: String): FurnitureEntity?

    @Query("SELECT * FROM furniture WHERE category = :category ORDER BY name")
    fun getFurnitureByCategory(category: String): Flow<List<FurnitureEntity>>

    @Query("SELECT * FROM furniture WHERE isCustom = 0 ORDER BY category, name")
    fun getCatalogFurniture(): Flow<List<FurnitureEntity>>

    @Query("SELECT * FROM furniture WHERE isCustom = 1 ORDER BY updatedAt DESC")
    fun getCustomFurniture(): Flow<List<FurnitureEntity>>

    @Query("SELECT * FROM furniture WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY name")
    fun searchFurniture(query: String): Flow<List<FurnitureEntity>>

    @Query("SELECT * FROM furniture WHERE price BETWEEN :minPrice AND :maxPrice ORDER BY price")
    fun getFurnitureByPriceRange(minPrice: Double, maxPrice: Double): Flow<List<FurnitureEntity>>

    @Query("SELECT DISTINCT category FROM furniture WHERE isCustom = 0 ORDER BY category")
    suspend fun getAvailableCategories(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFurniture(furniture: FurnitureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFurnitures(furnitures: List<FurnitureEntity>)

    @Update
    suspend fun updateFurniture(furniture: FurnitureEntity)

    @Delete
    suspend fun deleteFurniture(furniture: FurnitureEntity)

    @Query("DELETE FROM furniture WHERE id = :id")
    suspend fun deleteFurnitureById(id: String)

    @Query("DELETE FROM furniture WHERE isCustom = 1")
    suspend fun deleteAllCustomFurniture()
}
