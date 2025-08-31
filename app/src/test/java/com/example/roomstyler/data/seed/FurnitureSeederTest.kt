package com.example.roomstyler.data.seed

import org.junit.Test
import org.junit.Assert.*

class FurnitureSeederTest {

    @Test
    fun `getCategories returns expected number of categories`() {
        // When
        val categories = FurnitureSeeder.getCategories()

        // Then
        assertEquals(8, categories.size)
        
        // Check that all expected categories are present
        val categoryNames = categories.map { it.nameEn }
        assertTrue(categoryNames.contains("DESK"))
        assertTrue(categoryNames.contains("CHAIR"))
        assertTrue(categoryNames.contains("SOFA"))
        assertTrue(categoryNames.contains("TABLE"))
        assertTrue(categoryNames.contains("BED"))
        assertTrue(categoryNames.contains("SHELF"))
        assertTrue(categoryNames.contains("TV"))
        assertTrue(categoryNames.contains("STORAGE"))
    }

    @Test
    fun `getFurniture returns expected furniture items`() {
        // When
        val furniture = FurnitureSeeder.getFurniture()

        // Then
        assertTrue("Should have furniture items", furniture.isNotEmpty())
        assertTrue("Should have at least 15 items", furniture.size >= 15)
        
        // Check that furniture has required fields
        furniture.forEach { item ->
            assertNotNull("ID should not be null", item.id)
            assertNotNull("Category should not be null", item.category)
            assertNotNull("Name should not be null", item.name)
            assertTrue("Width should be positive", item.sizeW > 0)
            assertTrue("Depth should be positive", item.sizeD > 0)
            assertTrue("Height should be positive", item.sizeH > 0)
        }
    }

    @Test
    fun `categories have proper sort order`() {
        // When
        val categories = FurnitureSeeder.getCategories()

        // Then
        val sortOrders = categories.map { it.sortOrder }
        val sortedOrders = sortOrders.sorted()
        assertEquals("Categories should be properly ordered", sortedOrders, sortOrders)
    }

    @Test
    fun `furniture items have valid categories`() {
        // Given
        val validCategories = FurnitureSeeder.getCategories().map { it.nameEn }.toSet()
        
        // When
        val furniture = FurnitureSeeder.getFurniture()

        // Then
        furniture.forEach { item ->
            assertTrue(
                "Furniture category ${item.category} should be valid",
                validCategories.contains(item.category)
            )
        }
    }

    @Test
    fun `furniture items have reasonable prices`() {
        // When
        val furniture = FurnitureSeeder.getFurniture()

        // Then
        furniture.forEach { item ->
            assertNotNull("Price should not be null", item.price)
            assertTrue("Price should be positive", item.price!! > 0)
            assertTrue("Price should be reasonable (< 1,000,000)", item.price!! < 1_000_000)
        }
    }

    @Test
    fun `furniture items have model paths`() {
        // When
        val furniture = FurnitureSeeder.getFurniture()

        // Then
        furniture.forEach { item ->
            assertNotNull("Model path should not be null", item.modelPath)
            assertTrue("Model path should not be empty", item.modelPath!!.isNotEmpty())
        }
    }
}
