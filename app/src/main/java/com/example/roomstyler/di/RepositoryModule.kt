package com.example.roomstyler.di

import com.example.roomstyler.ar.repository.ArSceneRepository
import com.example.roomstyler.ar.repository.ArSceneRepositoryImpl
import com.example.roomstyler.data.repository.FurnitureRepositoryImpl
import com.example.roomstyler.data.repository.LayoutRepositoryImpl
import com.example.roomstyler.data.repository.SnapshotRepositoryImpl
import com.example.roomstyler.domain.repository.FurnitureRepository
import com.example.roomstyler.domain.repository.LayoutRepository
import com.example.roomstyler.domain.repository.SnapshotRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFurnitureRepository(
        furnitureRepositoryImpl: FurnitureRepositoryImpl
    ): FurnitureRepository

    @Binds
    @Singleton
    abstract fun bindLayoutRepository(
        layoutRepositoryImpl: LayoutRepositoryImpl
    ): LayoutRepository
    
    @Binds
    @Singleton
    abstract fun bindSnapshotRepository(
        snapshotRepositoryImpl: SnapshotRepositoryImpl
    ): SnapshotRepository
    
    @Binds
    @Singleton
    abstract fun bindArSceneRepository(
        arSceneRepositoryImpl: ArSceneRepositoryImpl
    ): ArSceneRepository
}
