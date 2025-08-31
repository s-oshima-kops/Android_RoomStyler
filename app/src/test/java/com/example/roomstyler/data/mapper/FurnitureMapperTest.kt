package com.example.roomstyler.data.mapper

import com.example.roomstyler.data.local.entity.FurnitureEntity
import com.example.roomstyler.domain.model.Furniture
import com.example.roomstyler.domain.model.Size3D
import org.junit.Test
import org.junit.Assert.*

class FurnitureMapperTest {

    @Test
    fun `FurnitureEntity to Furniture mapping works correctly`() {
        // Given
        val entity = FurnitureEntity(
            id = "test-id",
            category = "DESK",
            name = "テストデスク",
            description = "テスト用のデスク",
            sizeW = 120.0,
            sizeD = 60.0,
            sizeH = 75.0,
            material = "木材",
            color = "ナチュラル",
            price = 15000.0,
            brand = "テストブランド",
            isCustom = false
        )

        // When
        val furniture = entity.toModel()

        // Then
        assertEquals("test-id", furniture.id)
        assertEquals("DESK", furniture.category)
        assertEquals(Size3D(120.0, 60.0, 75.0), furniture.sizeCm)
        assertEquals("木材", furniture.material)
        assertEquals("ナチュラル", furniture.color)
        assertEquals("テストデスク", furniture.meta["name"])
        assertEquals("テスト用のデスク", furniture.meta["description"])
        assertEquals("15000.0", furniture.meta["price"])
        assertEquals("テストブランド", furniture.meta["brand"])
        assertEquals("false", furniture.meta["isCustom"])
    }

    @Test
    fun `Furniture to FurnitureEntity mapping works correctly`() {
        // Given
        val furniture = Furniture(
            id = "test-id",
            category = "CHAIR",
            sizeCm = Size3D(50.0, 50.0, 85.0),
            material = "ファブリック",
            color = "グレー",
            meta = mapOf(
                "name" to "テストチェア",
                "description" to "快適なチェア",
                "price" to "20000.0",
                "brand" to "テストメーカー",
                "isCustom" to "true"
            )
        )

        // When
        val entity = furniture.toEntity()

        // Then
        assertEquals("test-id", entity.id)
        assertEquals("CHAIR", entity.category)
        assertEquals("テストチェア", entity.name)
        assertEquals("快適なチェア", entity.description)
        assertEquals(50.0, entity.sizeW, 0.001)
        assertEquals(50.0, entity.sizeD, 0.001)
        assertEquals(85.0, entity.sizeH, 0.001)
        assertEquals("ファブリック", entity.material)
        assertEquals("グレー", entity.color)
        assertEquals(20000.0, entity.price)
        assertEquals("テストメーカー", entity.brand)
        assertTrue(entity.isCustom)
    }

    @Test
    fun `mapping handles null values correctly`() {
        // Given
        val entity = FurnitureEntity(
            id = "test-id",
            category = "TABLE",
            name = "シンプルテーブル",
            sizeW = 100.0,
            sizeD = 50.0,
            sizeH = 40.0,
            description = null,
            price = null,
            brand = null,
            material = null,
            color = null
        )

        // When
        val furniture = entity.toModel()

        // Then
        assertEquals("test-id", furniture.id)
        assertEquals("TABLE", furniture.category)
        assertEquals("シンプルテーブル", furniture.meta["name"])
        assertNull(furniture.material)
        assertNull(furniture.color)
        assertFalse(furniture.meta.containsKey("description"))
        assertFalse(furniture.meta.containsKey("price"))
        assertFalse(furniture.meta.containsKey("brand"))
    }
}
