# Android模様替えアプリ 要件定義書（SRS）v2
作成日: 2025-08-30 / 対象: Android（minSdk 29）

---

## 0. 概要
- **目的**：スマホで撮影/スキャンした室内をもとに、複数の家具レイアウト案を自動生成・プレビュー・保存できるAndroidアプリ。
- **AI利用方針**：ChatGPTは「自然言語 → Layout Intent JSON」への変換に限定（画像生成は任意/将来拡張）。
- **無料ポリシー**：AR配置・レイアウト探索・静止画書き出しは無料。高画質生成/追加案は任意の上限や課金で補完。

---

## 1. スコープ
### 1.1 MVPに含める
- ARCoreによる床/壁検出、3D家具(glTF等)配置、通路/窓/扉を考慮した**制約ソルバ**で3〜5案生成。
- ARプレビューから**静止画出力**（端末保存/共有）。
- チャット指示（日本語）→ **Layout Intent JSON**（ChatGPT）→再配置。
- 家具カタログ（基本モデル、サイズ/色/カテゴリでフィルタ）。

### 1.2 後続（Phase 2+）
- 画像インペイント等の**フォトリアル合成**（回数制限/高画質はオプション）。
- EC連携（アフィリエイト）、価格帯/在庫で候補提示。
- 間取り図インポート、マルチルーム。

### 1.3 非スコープ（MVP時点）
- 正確な室内寸法の自動復元（LiDARなしの完全自動化）。
- 実家具の完全な素材/光源一致の写実レンダリング。

---

## 2. ユースケース
- **UC-01**：写真/ARスキャンからレイアウト案を**3案以上**提示。
- **UC-02**：自然言語で「幅120×奥行60の机を窓際に」→Intent JSON→再配置。
- **UC-03**：ARプレビューのフレームをPNG保存/共有。
- **UC-04**：家具カタログから追加/差し替え。

---

## 3. 機能要件（FR）
- **FR-01**：床/壁の平面検出と原点設定、基準物(A4等)で**スケール校正**。
- **FR-02**：家電/扉/窓位置の簡易指定UI（手動マーキング）。
- **FR-03**：制約ソルバで**衝突回避/通路幅≥0.6m**などを満たす案を最大N件生成。
- **FR-04**：各案に**スコア/理由**（制約満足度）を付与。
- **FR-05**：自然言語→Layout Intent JSON（ChatGPT）。BYO APIキー対応。
- **FR-06**：ARプレビューからPNG書き出し、ギャラリー保存、共有シート連携。
- **FR-07**：オフライン時は**GUI指定のみ**で再配置可能。

---

## 4. 非機能要件（NFR）
- **対応OS**：Android 10+（minSdk 29）。
- **性能**：Pixel 7相当で3案生成≤10秒。
- **可用性**：クラッシュフリー率≥99.5%/月、ANR<0.47%/日。
- **オフライン**：AR/制約ソルバ/書き出しは完全ローカル。
- **プライバシ**：画像/深度は端末保存。クラウド送信はユーザー許可時のみ。

---

## 5. アーキテクチャ/技術選定
- **構成**：Clean Architecture（MVI）。`ui(Compose)` / `domain(ソルバ/ユースケース)` / `data(Repo/Room)` / `ar(ARCore/SceneView)` / `ml(任意)`
- **主要技術**：Kotlin、Jetpack Compose、CameraX、ARCore(+SceneView/Filament)、Hilt、Room、kotlinx-serialization、Coil。
- **LLM**：OpenAI（ChatGPT）JSONモード。出力は**厳格JSON**のみを受理。

---

## 6. データモデル（抜粋）
```kotlin
@Serializable data class Size3D(val w: Double, val d: Double, val h: Double)
@Serializable data class Pose3D(val x: Double, val y: Double, val z: Double, val yaw: Double, val pitch: Double, val roll: Double)

@Serializable data class Furniture(
  val id: String,
  val category: String, // DESK, CHAIR, SOFA, SHELF, BED, TABLE, TV, STORAGE, ETC
  val sizeCm: Size3D,
  val position: Pose3D? = null,
  val material: String? = null,
  val color: String? = null,
  val meta: Map<String, String> = emptyMap()
)

@Serializable data class Room(
  val widthM: Double, val depthM: Double, val heightM: Double,
  val windows: List<String> = emptyList(), // 簡易表現（MVP）
  val doors: List<String> = emptyList(),
  val powerOutlets: List<String> = emptyList()
)

@Serializable data class Layout(
  val id: String,
  val room: Room,
  val furnitures: List<Furniture],
  val score: Double,
  val reasons: List<String>
)
```

---

## 7. 制約ソルバ仕様（MVP）
- **通路幅**：主要動線0.6m以上を確保。
- **扉/窓**：扉開閉角付近の障害物ゼロ、窓前0.3m確保。
- **TV視距離**：画面対角×1.5〜2.5。
- **机の採光**：`near_window`時は窓中心±1.2m範囲を優先。
- **スコア**：`score = Σ w_i * sat_i`（0..1）。
- **探索**：ランダム初期化→局所探索（SA or Hill Climbing）→上位N件。

---

## 8. LLM連携仕様
- **役割**：自然言語→**Layout Intent JSON**。
- **JSONスキーマ**：
```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "title": "LayoutIntent",
  "type": "object",
  "properties": {
    "action": {"type": "string", "enum": ["add","move","remove","replace","generate_options"]},
    "target": {"type": "string"},
    "size_cm": {"type": "object","properties":{"w":{"type":"number"},"d":{"type":"number"},"h":{"type":"number"}}},
    "constraints": {"type": "array","items":{"type":"string"}},
    "position_hint": {"type":"string"},
    "count": {"type":"integer","minimum":1,"default":3}
  },
  "required": ["action"],
  "additionalProperties": true
}
```
- **例**（「幅120×奥行60の机を窓際に」）：
```json
{"action":"add","target":"desk","size_cm":{"w":120,"d":60,"h":75},"constraints":["keep_walkway>=60cm"],"position_hint":"near_window","count":4}
```
- **ガード**：JSON以外の文字を除去→パース失敗時は再プロンプト/GUIフォールバック。

---

## 9. 画面/UX
- Home（写真/AR/最近の案）、Scan/Load、Proposals（カード: スコア/理由）、Preview（AR撮影・保存）、Catalog、Chat（指示→JSON→再配置）。

---

## 10. セキュリティ/プライバシ
- キー管理：EncryptedSharedPreferences。送信ログなし（デフォルト）。画像は端末保存。

---

## 11. 収益/無料維持
- **無料**：AR配置、レイアウト探索、PNG書き出し。
- **収益**：高画質生成、追加案、ECアフィリエイト、広告（非侵襲）。

---

## 12. 受け入れ基準（抜粋）
- Pixel 7相当で**3案≤10秒**、PNG書き出し可。
- ChatGPTなしでもGUI指定で案生成可能。
- ChatGPTありで自然言語→Intent→再配置が機能。

---

## 13. テスト観点
- 平面検出安定性、校正誤差（A4で±3%以内）、通路/扉/窓制約、案生成時間、JSONパース耐性、オフライン動作。

---

## 14. リスク/対策
- **寸法誤差**→基準物/手動補正UI。
- **端末性能差**→探索回数/解像度の段階設定。
- **LLM不整合**→JSONモード+厳格パース+フォールバック。
- **素材権利**→ライセンス明確な3Dモデルのみ。

---

## 15. リリース計画
- **P1**：AR配置+制約ソルバ+PNG。
- **P2**：ChatGPT連携、カタログ拡充。
- **P3**：写真合成、課金/EC連携。
