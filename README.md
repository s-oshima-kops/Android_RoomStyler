# RoomStyler - AR家具配置アプリ

## 概要
RoomStylerは、ARCore技術を使用してリアルタイムで家具の配置をシミュレーションできるAndroidアプリケーションです。

## 主な機能
- **ARスキャン**: カメラを使用した部屋のスキャンと平面検出
- **家具配置**: AR空間での3Dオブジェクトの配置・操作
- **レイアウト提案**: 制約ソルバーによる最適な家具配置の提案
- **データ保存**: Room DBを使用した配置データの永続化
- **カタログ**: 家具アイテムのブラウジング機能

## 技術スタック
- **言語**: Kotlin
- **UI**: Jetpack Compose
- **アーキテクチャ**: Clean Architecture (ui/domain/data/ar/ml)
- **DI**: Hilt
- **データベース**: Room
- **AR**: ARCore + SceneView
- **ナビゲーション**: Compose Navigation
- **シリアライゼーション**: kotlinx-serialization

## 必要な環境
- Android Studio Hedgehog | 2023.1.1 以降
- Android SDK 29 以上
- ARCore対応デバイス

## セットアップ
1. リポジトリをクローン
```bash
git clone https://github.com/s-oshima-kops/Android_RoomStyler.git
```

2. Android Studioでプロジェクトを開く

3. Gradle同期を実行

4. ARCore対応の実機でアプリを実行

## プロジェクト構成
```
Android_RoomStyler/
├── app/src/main/java/com/example/roomstyler/
│   ├── ui/           # UI層 (Compose画面・ViewModel)
│   ├── domain/       # ドメイン層 (ビジネスロジック・モデル)
│   ├── data/         # データ層 (Repository・DB・API)
│   ├── ar/           # AR機能 (SceneView・ARCore)
│   └── di/           # 依存性注入 (Hilt)
└── doc/              # プロジェクトドキュメント
    ├── README.md                    # ドキュメント一覧
    ├── Android_RoomStyler_SRS_v2.md # 要件仕様書
    ├── TEST_PLAN.md                 # テスト計画
    └── その他開発ドキュメント
```

## 画面構成
- **HomeScreen**: メイン画面・レイアウト一覧
- **ScanScreen**: ARスキャン・オブジェクト配置
- **ProposalsScreen**: レイアウト提案表示
- **PreviewScreen**: 配置プレビュー
- **CatalogScreen**: 家具カタログ
- **ChatScreen**: 自然言語による配置指示

## 開発状況
- ✅ P-01: プロジェクト基盤構築
- ✅ P-02: AR配置の骨組み
- ✅ P-03: データモデルと保存層
- ✅ P-04: レイアウト制約ソルバ（MVP）
- 🚧 P-05: Chat → Layout Intent JSON（ダミー）
- ⏳ P-06: OpenAI連携（本実装・BYOキー）
- ⏳ P-07: ARプレビューの静止画書き出し
- ⏳ P-08: カタログ & 追加/差し替え
- ⏳ P-09: テレメトリ/QA
- ⏳ P-10: 写真合成の雛形（任意）

## ドキュメント
詳細なドキュメントは [`doc/`](./doc/) ディレクトリを参照してください：

- **[要件仕様書](./doc/Android_RoomStyler_SRS_v2.md)** - プロジェクト要件定義
- **[テスト計画](./doc/TEST_PLAN.md)** - テスト戦略・手順
- **[開発ガイド](./doc/)** - ビルド・トラブルシューティング

## ライセンス
MIT License
