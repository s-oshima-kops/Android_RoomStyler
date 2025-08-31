package com.example.roomstyler.data.mapper

import com.example.roomstyler.data.local.entity.FurnitureEntity
import com.example.roomstyler.domain.model.Furniture
import com.example.roomstyler.domain.model.Pose3D
import com.example.roomstyler.domain.model.Size3D
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val gson = Gson()

fun FurnitureEntity.toModel(): Furniture {
    val position = if (positionX != null && positionY != null && positionZ != null &&
        yaw != null && pitch != null && roll != null) {
        Pose3D(positionX, positionY, positionZ, yaw, pitch, roll)
    } else null

    val metaType = object : TypeToken<Map<String, String>>() {}.type
    val meta = try {
        gson.fromJson<Map<String, String>>(metaJson, metaType) ?: emptyMap()
    } catch (e: Exception) {
        emptyMap()
    }

    return Furniture(
        id = id,
        category = category,
        sizeCm = Size3D(sizeW, sizeD, sizeH),
        position = position,
        material = material,
        color = color,
        meta = meta.toMutableMap().apply {
            put("name", name)
            description?.let { put("description", it) }
            price?.let { put("price", it.toString()) }
            brand?.let { put("brand", it) }
            modelPath?.let { put("modelPath", it) }
            thumbnailUrl?.let { put("thumbnailUrl", it) }
            put("isCustom", isCustom.toString())
        }
    )
}

fun Furniture.toEntity(): FurnitureEntity {
    val name = meta["name"] ?: "Unknown"
    val description = meta["description"]
    val price = meta["price"]?.toDoubleOrNull()
    val brand = meta["brand"]
    val modelPath = meta["modelPath"]
    val thumbnailUrl = meta["thumbnailUrl"]
    val isCustom = meta["isCustom"]?.toBooleanStrictOrNull() ?: false
    
    // Remove these from meta to avoid duplication
    val cleanMeta = meta.toMutableMap().apply {
        remove("name")
        remove("description")
        remove("price")
        remove("brand")
        remove("modelPath")
        remove("thumbnailUrl")
        remove("isCustom")
    }

    return FurnitureEntity(
        id = id,
        category = category,
        name = name,
        description = description,
        sizeW = sizeCm.w,
        sizeD = sizeCm.d,
        sizeH = sizeCm.h,
        positionX = position?.x,
        positionY = position?.y,
        positionZ = position?.z,
        yaw = position?.yaw,
        pitch = position?.pitch,
        roll = position?.roll,
        material = material,
        color = color,
        price = price,
        brand = brand,
        modelPath = modelPath,
        thumbnailUrl = thumbnailUrl,
        isCustom = isCustom,
        metaJson = gson.toJson(cleanMeta)
    )
}
