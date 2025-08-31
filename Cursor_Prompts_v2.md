# Cursor用プロンプト集 v2（Android模様替えアプリ）
作成日: 2025-08-30

> 使い方：以下を**順番に**Cursorへ貼って実行し、コミットを分けて進めます。  
> 依存バージョンは「最新安定」を指定（固定が必要な場合のみ数値化）。

---

## P-01 リポジトリ初期化
```
あなたはAndroidエンジニアです。以下の要件でプロジェクトを新規作成してください。
- Kotlin / Jetpack Compose / Hilt / Room / ARCore(SceneView)
- パッケージ: com.example.roomstyler
- 画面: HomeScreen, ScanScreen, ProposalsScreen, PreviewScreen, CatalogScreen, ChatScreen
- Clean Architecture (ui/domain/data/ar/ml) の雛形
- ビルド成功までセットアップ（minSdk 29 / target 最新安定）
```

## P-02 AR配置の骨組み
```
ARCore + SceneViewで床平面検出を有効化し、タップ位置に仮ボックス(glTFの立方体)を配置するデモを実装。
- 平面検出ON、アンカー設置
- 配置したボックスの移動/回転/削除をジェスチャ対応
- 現在の家具配置をJSONで保存/読み込み（kotlinx-serialization）
```

## P-03 データモデルと保存層
```
SRSのデータモデル（Furniture/Room/Layout等）を作成し、Room DBとRepositoryを実装。
- 配置スナップショットの保存/取得
- Catalog用のダミー家具データを投入
```

## P-04 レイアウト制約ソルバ（MVP）
```
domain層に制約ソルバを実装。要件は：
- 通路幅>=0.6m、扉/窓の簡易回避、TV視距離、near_windowヒントの優先
- ランダム初期化→局所探索でscore最大化
- 3〜5案生成し、ProposalsScreenにスコア順で表示（カードにscoreとreasons）
```

## P-05 Chat → Layout Intent JSON（ダミー）
```
ChatScreenで自然言語入力→ダミー関数でIntent JSON（SRSのスキーマ）を返す流れを作成。
- 返却JSONをソルバに渡して再計算→Proposals更新
- JSON以外は拒否/再入力を促すバリデーション
```

## P-06 OpenAI連携（本実装・BYOキー）
```
OpenAIのJSONモードを使って自然言語→Layout Intent JSONを生成。
- EncryptedSharedPreferencesでAPIキー保存/読込UI
- タイムアウト/再試行/レート制御
- 先頭/末尾の不要文字除去、最初の{〜最後の}抽出、kotlinx-serializationで厳格パース
```

## P-07 ARプレビューの静止画書き出し
```
PreviewScreenから現在のARフレームをPNG保存/共有
- 権限ハンドリング
- Pictures/RoomStyler 配下にファイル保存
- 成功/失敗のトーストとギャラリー連携
```

## P-08 カタログ & 追加/差し替え
```
CatalogScreenでカテゴリ/サイズ/色のフィルタと、選択家具の追加/差し替えを実装。
- 追加/差し替え結果をソルバへ反映→Proposals更新
```

## P-09 テレメトリ/QA
```
- 案生成時間、制約違反数、スコア平均をローカルログに記録（個人データなし）
- ユニットテスト: スコア関数、通路幅/扉回避
- UIテスト: ProposalsScreenのカード表示
```

## P-10（任意）写真合成の雛形
```
TFLiteの簡易インペイントで低解像度プレビューを実装（参考用）
- 合成は見栄えのみで、最終判断はARプレビューを推奨する文言をUIに表示
```

---

## 補助：ChatGPT（JSON変換）用プロンプト

**System（固定）**
```
あなたはインテリアのレイアウト設計アシスタントです。ユーザーの日本語指示を、指定のJSONスキーマ(LayoutIntent)で厳格なJSONのみ出力します。説明文は書かないでください。単位はセンチ。位置ヒントは near_window / near_wall_north / near_wall_south / near_wall_east / near_wall_west / center / corner を使用。未指定値は出力しないでください。数値は数値型で出力してください。
```

**User（テンプレート）**
```
ユーザー指示: "{instruction}"
既知の部屋メタデータ: {room_meta_json}
既存家具一覧: {furniture_list_json}
JSONスキーマ: {schema}
→ これを満たすLayoutIntentをJSONだけで返してください。
```
