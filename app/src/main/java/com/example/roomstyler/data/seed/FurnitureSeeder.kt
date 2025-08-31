package com.example.roomstyler.data.seed

import com.example.roomstyler.data.local.entity.CategoryEntity
import com.example.roomstyler.data.local.entity.FurnitureEntity
import java.util.UUID

object FurnitureSeeder {
    
    fun getCategories(): List<CategoryEntity> {
        return listOf(
            CategoryEntity(
                id = "desk",
                name = "デスク",
                nameEn = "DESK",
                description = "作業用デスク・学習机",
                color = "#2196F3",
                sortOrder = 1
            ),
            CategoryEntity(
                id = "chair",
                name = "チェア",
                nameEn = "CHAIR",
                description = "椅子・チェア",
                color = "#4CAF50",
                sortOrder = 2
            ),
            CategoryEntity(
                id = "sofa",
                name = "ソファ",
                nameEn = "SOFA",
                description = "ソファ・ソファベッド",
                color = "#FF9800",
                sortOrder = 3
            ),
            CategoryEntity(
                id = "table",
                name = "テーブル",
                nameEn = "TABLE",
                description = "ダイニングテーブル・コーヒーテーブル",
                color = "#9C27B0",
                sortOrder = 4
            ),
            CategoryEntity(
                id = "bed",
                name = "ベッド",
                nameEn = "BED",
                description = "ベッド・マットレス",
                color = "#E91E63",
                sortOrder = 5
            ),
            CategoryEntity(
                id = "shelf",
                name = "シェルフ",
                nameEn = "SHELF",
                description = "本棚・収納棚",
                color = "#795548",
                sortOrder = 6
            ),
            CategoryEntity(
                id = "tv",
                name = "TV・AV機器",
                nameEn = "TV",
                description = "テレビ・AVボード",
                color = "#607D8B",
                sortOrder = 7
            ),
            CategoryEntity(
                id = "storage",
                name = "収納",
                nameEn = "STORAGE",
                description = "クローゼット・収納ボックス",
                color = "#FFC107",
                sortOrder = 8
            )
        )
    }
    
    fun getFurniture(): List<FurnitureEntity> {
        return listOf(
            // デスク
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "DESK",
                name = "シンプルデスク 120cm",
                description = "シンプルなデザインの作業デスク。幅120cm、奥行60cm。",
                sizeW = 120.0,
                sizeD = 60.0,
                sizeH = 75.0,
                material = "木材",
                color = "ナチュラル",
                price = 15000.0,
                brand = "IKEA",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "DESK",
                name = "L字デスク",
                description = "コーナーに設置できるL字型デスク。効率的な作業スペース。",
                sizeW = 150.0,
                sizeD = 120.0,
                sizeH = 75.0,
                material = "木材",
                color = "ホワイト",
                price = 25000.0,
                brand = "ニトリ",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "DESK",
                name = "スタンディングデスク",
                description = "高さ調整可能なスタンディングデスク。健康的な作業環境。",
                sizeW = 100.0,
                sizeD = 50.0,
                sizeH = 110.0,
                material = "スチール",
                color = "ブラック",
                price = 35000.0,
                brand = "オカムラ",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            
            // チェア
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "CHAIR",
                name = "オフィスチェア",
                description = "エルゴノミクス設計のオフィスチェア。長時間の作業に最適。",
                sizeW = 60.0,
                sizeD = 60.0,
                sizeH = 120.0,
                material = "メッシュ",
                color = "ブラック",
                price = 20000.0,
                brand = "コクヨ",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "CHAIR",
                name = "ダイニングチェア",
                description = "シンプルなダイニングチェア。4脚セット。",
                sizeW = 45.0,
                sizeD = 50.0,
                sizeH = 85.0,
                material = "木材",
                color = "ナチュラル",
                price = 8000.0,
                brand = "無印良品",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            
            // ソファ
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "SOFA",
                name = "3人掛けソファ",
                description = "ゆったりとした3人掛けソファ。リビングの中心に。",
                sizeW = 200.0,
                sizeD = 90.0,
                sizeH = 85.0,
                material = "ファブリック",
                color = "グレー",
                price = 80000.0,
                brand = "カリモク",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "SOFA",
                name = "2人掛けソファ",
                description = "コンパクトな2人掛けソファ。一人暮らしにも最適。",
                sizeW = 150.0,
                sizeD = 80.0,
                sizeH = 80.0,
                material = "レザー",
                color = "ブラウン",
                price = 50000.0,
                brand = "ニトリ",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            
            // テーブル
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "TABLE",
                name = "ダイニングテーブル",
                description = "4人用ダイニングテーブル。家族での食事に。",
                sizeW = 150.0,
                sizeD = 80.0,
                sizeH = 75.0,
                material = "木材",
                color = "ダークブラウン",
                price = 40000.0,
                brand = "IKEA",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "TABLE",
                name = "コーヒーテーブル",
                description = "リビング用コーヒーテーブル。雑誌や小物を置くのに便利。",
                sizeW = 100.0,
                sizeD = 50.0,
                sizeH = 40.0,
                material = "ガラス",
                color = "クリア",
                price = 15000.0,
                brand = "無印良品",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            
            // ベッド
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "BED",
                name = "シングルベッド",
                description = "シンプルなシングルベッドフレーム。マットレス別売り。",
                sizeW = 100.0,
                sizeD = 200.0,
                sizeH = 85.0,
                material = "木材",
                color = "ナチュラル",
                price = 25000.0,
                brand = "ニトリ",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "BED",
                name = "ダブルベッド",
                description = "ゆったりとしたダブルベッド。2人でも快適。",
                sizeW = 140.0,
                sizeD = 200.0,
                sizeH = 85.0,
                material = "木材",
                color = "ホワイト",
                price = 45000.0,
                brand = "IKEA",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            
            // シェルフ
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "SHELF",
                name = "本棚 5段",
                description = "5段の本棚。大容量で本や小物を整理。",
                sizeW = 80.0,
                sizeD = 30.0,
                sizeH = 180.0,
                material = "木材",
                color = "ダークブラウン",
                price = 12000.0,
                brand = "無印良品",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "SHELF",
                name = "オープンシェルフ",
                description = "オープンタイプのシェルフ。見せる収納に最適。",
                sizeW = 120.0,
                sizeD = 35.0,
                sizeH = 150.0,
                material = "スチール",
                color = "ブラック",
                price = 18000.0,
                brand = "ニトリ",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            
            // TV・AV機器
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "TV",
                name = "TVボード 150cm",
                description = "150cmのTVボード。大型テレビにも対応。",
                sizeW = 150.0,
                sizeD = 40.0,
                sizeH = 50.0,
                material = "木材",
                color = "ウォルナット",
                price = 30000.0,
                brand = "カリモク",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "TV",
                name = "コンパクトTVスタンド",
                description = "小型テレビ用のコンパクトなスタンド。",
                sizeW = 80.0,
                sizeD = 30.0,
                sizeH = 45.0,
                material = "木材",
                color = "ホワイト",
                price = 8000.0,
                brand = "IKEA",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            
            // 収納
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "STORAGE",
                name = "クローゼット",
                description = "大容量のクローゼット。衣類をたっぷり収納。",
                sizeW = 120.0,
                sizeD = 60.0,
                sizeH = 200.0,
                material = "木材",
                color = "ホワイト",
                price = 60000.0,
                brand = "ニトリ",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            ),
            FurnitureEntity(
                id = UUID.randomUUID().toString(),
                category = "STORAGE",
                name = "チェスト 4段",
                description = "4段のチェスト。衣類や小物の整理に。",
                sizeW = 80.0,
                sizeD = 40.0,
                sizeH = 100.0,
                material = "木材",
                color = "ナチュラル",
                price = 20000.0,
                brand = "無印良品",
                modelPath = "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf"
            )
        )
    }
}
