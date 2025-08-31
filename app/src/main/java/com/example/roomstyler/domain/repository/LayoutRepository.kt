package com.example.roomstyler.domain.repository

import com.example.roomstyler.domain.model.Layout
import kotlinx.coroutines.flow.Flow

interface LayoutRepository {
    fun getAllLayouts(): Flow<List<Layout>>
    suspend fun getLayoutById(id: String): Layout?
    fun getRecentLayouts(limit: Int = 10): Flow<List<Layout>>
    suspend fun insertLayout(layout: Layout)
    suspend fun insertLayouts(layouts: List<Layout>)
    suspend fun updateLayout(layout: Layout)
    suspend fun deleteLayout(layout: Layout)
    suspend fun deleteLayoutById(id: String)
}
