package com.example.roomstyler.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.example.roomstyler.data.local.dao.CategoryDao
import com.example.roomstyler.data.local.dao.FurnitureDao
import com.example.roomstyler.data.local.dao.LayoutDao
import com.example.roomstyler.data.local.dao.SnapshotDao
import com.example.roomstyler.data.local.entity.CategoryEntity
import com.example.roomstyler.data.local.entity.FurnitureEntity
import com.example.roomstyler.data.local.entity.LayoutEntity
import com.example.roomstyler.data.local.entity.SnapshotEntity

@Database(
    entities = [
        FurnitureEntity::class,
        LayoutEntity::class,
        SnapshotEntity::class,
        CategoryEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RoomStylerDatabase : RoomDatabase() {
    abstract fun furnitureDao(): FurnitureDao
    abstract fun layoutDao(): LayoutDao
    abstract fun snapshotDao(): SnapshotDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val DATABASE_NAME = "roomstyler_database"
        
        val MIGRATION_1_2 = object : Migration(1, 2) {
                            override fun migrate(db: SupportSQLiteDatabase) {
                // Add new columns to furniture table
                db.execSQL("""
                    ALTER TABLE furniture ADD COLUMN name TEXT NOT NULL DEFAULT 'Unknown'
                """)
                db.execSQL("""
                    ALTER TABLE furniture ADD COLUMN description TEXT
                """)
                db.execSQL("""
                    ALTER TABLE furniture ADD COLUMN price REAL
                """)
                db.execSQL("""
                    ALTER TABLE furniture ADD COLUMN brand TEXT
                """)
                db.execSQL("""
                    ALTER TABLE furniture ADD COLUMN modelPath TEXT
                """)
                db.execSQL("""
                    ALTER TABLE furniture ADD COLUMN thumbnailUrl TEXT
                """)
                db.execSQL("""
                    ALTER TABLE furniture ADD COLUMN isCustom INTEGER NOT NULL DEFAULT 0
                """)
                db.execSQL("""
                    ALTER TABLE furniture ADD COLUMN createdAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}
                """)
                db.execSQL("""
                    ALTER TABLE furniture ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}
                """)
                
                // Add new columns to layout table
                db.execSQL("""
                    ALTER TABLE layout ADD COLUMN name TEXT NOT NULL DEFAULT 'Layout'
                """)
                db.execSQL("""
                    ALTER TABLE layout ADD COLUMN description TEXT
                """)
                db.execSQL("""
                    ALTER TABLE layout ADD COLUMN isSnapshot INTEGER NOT NULL DEFAULT 0
                """)
                db.execSQL("""
                    ALTER TABLE layout ADD COLUMN snapshotImagePath TEXT
                """)
                db.execSQL("""
                    ALTER TABLE layout ADD COLUMN arSceneJson TEXT
                """)
                db.execSQL("""
                    ALTER TABLE layout ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}
                """)
                
                // Create snapshot table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS snapshot (
                        id TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        description TEXT,
                        layoutId TEXT NOT NULL,
                        imagePath TEXT NOT NULL,
                        thumbnailPath TEXT,
                        arSceneJson TEXT NOT NULL,
                        furnitureCount INTEGER NOT NULL DEFAULT 0,
                        roomDimensions TEXT NOT NULL,
                        tags TEXT NOT NULL DEFAULT '[]',
                        isFavorite INTEGER NOT NULL DEFAULT 0,
                        createdAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()},
                        updatedAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}
                    )
                """)
                
                // Create category table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS category (
                        id TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        nameEn TEXT NOT NULL,
                        description TEXT,
                        iconPath TEXT,
                        color TEXT,
                        sortOrder INTEGER NOT NULL DEFAULT 0,
                        isActive INTEGER NOT NULL DEFAULT 1,
                        createdAt INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}
                    )
                """)
            }
        }
    }
}
