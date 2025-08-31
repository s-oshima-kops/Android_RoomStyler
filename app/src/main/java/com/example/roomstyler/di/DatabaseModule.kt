package com.example.roomstyler.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomstyler.data.local.database.RoomStylerDatabase
import com.example.roomstyler.data.local.dao.CategoryDao
import com.example.roomstyler.data.local.dao.FurnitureDao
import com.example.roomstyler.data.local.dao.LayoutDao
import com.example.roomstyler.data.local.dao.SnapshotDao
import com.example.roomstyler.data.seed.FurnitureSeeder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomStylerDatabase(
        @ApplicationContext context: Context
    ): RoomStylerDatabase {
        return Room.databaseBuilder(
            context,
            RoomStylerDatabase::class.java,
            RoomStylerDatabase.DATABASE_NAME
        )
        .addMigrations(RoomStylerDatabase.MIGRATION_1_2)
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed data on first creation
                CoroutineScope(Dispatchers.IO).launch {
                    val database = Room.databaseBuilder(
                        context,
                        RoomStylerDatabase::class.java,
                        RoomStylerDatabase.DATABASE_NAME
                    ).build()
                    
                    // Insert categories
                    val categories = FurnitureSeeder.getCategories()
                    database.categoryDao().insertCategories(categories)
                    
                    // Insert furniture
                    val furniture = FurnitureSeeder.getFurniture()
                    database.furnitureDao().insertFurnitures(furniture)
                    
                    database.close()
                }
            }
        })
        .build()
    }

    @Provides
    fun provideFurnitureDao(database: RoomStylerDatabase): FurnitureDao {
        return database.furnitureDao()
    }

    @Provides
    fun provideLayoutDao(database: RoomStylerDatabase): LayoutDao {
        return database.layoutDao()
    }
    
    @Provides
    fun provideSnapshotDao(database: RoomStylerDatabase): SnapshotDao {
        return database.snapshotDao()
    }
    
    @Provides
    fun provideCategoryDao(database: RoomStylerDatabase): CategoryDao {
        return database.categoryDao()
    }
}
