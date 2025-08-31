package com.example.roomstyler.data.mapper

import com.example.roomstyler.data.local.entity.LayoutEntity
import com.example.roomstyler.domain.model.Layout
import com.example.roomstyler.domain.model.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val gson = Gson()

fun LayoutEntity.toModel(furnitures: List<com.example.roomstyler.domain.model.Furniture>): Layout {
    val stringListType = object : TypeToken<List<String>>() {}.type
    
    val windows = try {
        gson.fromJson<List<String>>(windowsJson, stringListType) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
    
    val doors = try {
        gson.fromJson<List<String>>(doorsJson, stringListType) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
    
    val powerOutlets = try {
        gson.fromJson<List<String>>(powerOutletsJson, stringListType) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
    
    val reasons = try {
        gson.fromJson<List<String>>(reasonsJson, stringListType) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    return Layout(
        id = id,
        room = Room(
            widthM = roomWidthM,
            depthM = roomDepthM,
            heightM = roomHeightM,
            windows = windows,
            doors = doors,
            powerOutlets = powerOutlets
        ),
        furnitures = furnitures,
        score = score,
        reasons = reasons
    )
}

fun Layout.toEntity(name: String = "Layout", description: String? = null, isSnapshot: Boolean = false): LayoutEntity {
    return LayoutEntity(
        id = id,
        name = name,
        description = description,
        roomWidthM = room.widthM,
        roomDepthM = room.depthM,
        roomHeightM = room.heightM,
        windowsJson = gson.toJson(room.windows),
        doorsJson = gson.toJson(room.doors),
        powerOutletsJson = gson.toJson(room.powerOutlets),
        furnituresJson = gson.toJson(furnitures),
        score = score,
        reasonsJson = gson.toJson(reasons),
        isSnapshot = isSnapshot
    )
}
