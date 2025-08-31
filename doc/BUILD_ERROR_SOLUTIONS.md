# ビルドエラー解決ガイド

## 🔧 ARCore関連エラー

### ✅ 解決済み: Manifest merger failed
```xml
<!-- 修正前 -->
<meta-data
    android:name="com.google.ar.core"
    android:value="required" />

<!-- 修正後 -->
<meta-data
    android:name="com.google.ar.core"
    android:value="required"
    tools:replace="android:value" />
```

## 🔧 よくある追加エラーと解決策

### エラー1: Duplicate class found
**症状**: 重複するクラスが見つかった
**解決策**: 
```kotlin
// build.gradle.kts (app) に追加
android {
    packagingOptions {
        pickFirst "**/libc++_shared.so"
        pickFirst "**/libjsc.so"
    }
}
```

### エラー2: Cannot resolve symbol 'ARSceneView'
**症状**: ARSceneViewが見つからない
**解決策**: 
1. File → Sync Project with Gradle Files
2. Build → Clean Project
3. Build → Rebuild Project

### エラー3: Minimum SDK version
**症状**: minSdkVersion が低すぎる
**解決策**: 
```kotlin
// build.gradle.kts (app) で確認
android {
    defaultConfig {
        minSdk = 29  // ARCoreには29以上が必要
    }
}
```

### エラー4: Missing internet permission
**症状**: ネットワークアクセスエラー
**解決策**: AndroidManifest.xmlに追加済み
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### エラー5: Gradle sync failed
**症状**: Gradle同期に失敗
**解決策**: 
1. File → Invalidate Caches and Restart
2. gradle.properties の確認
3. インターネット接続の確認

## 🚨 緊急時の対処法

### 完全リセット手順
1. **Android Studioを閉じる**
2. **プロジェクトフォルダで以下を削除:**
   ```
   .gradle/
   .idea/
   app/build/
   build/
   ```
3. **Android Studioでプロジェクトを再度開く**
4. **Gradle Syncを実行**

### バックアップからの復元
1. **Git履歴から復元** (推奨)
2. **手動でファイルを修正前に戻す**

## 📋 ビルド成功の確認

### ✅ 成功時の表示
```
BUILD SUCCESSFUL in 30s
47 actionable tasks: 47 executed
```

### ✅ APKファイルの生成確認
```
app/build/outputs/apk/debug/app-debug.apk
```

## 🔄 次のテスト手順

### Phase 1: ビルド成功後
1. **Run** → **Run 'app'** (Shift+F10)
2. **エミュレータまたは実機を選択**
3. **アプリの起動確認**

### Phase 2: 基本機能テスト
1. **ホーム画面の表示**
2. **画面遷移の動作**
3. **データベース初期化の確認**

### Phase 3: エラーログの確認
1. **View** → **Tool Windows** → **Logcat**
2. **フィルタ**: com.example.roomstyler
3. **エラーレベル**: Error/Warn

## 📞 サポート情報

### ログの確認方法
```
1. Android Studio下部の "Build" タブ
2. エラーメッセージの詳細を確認
3. 必要に応じてLogcatでランタイムエラーを確認
```

### よくある質問
**Q: ビルドは成功したがアプリが起動しない**
A: Logcatでランタイムエラーを確認してください

**Q: エミュレータでARが動作しない**
A: 正常です。AR機能は実機でのみ動作します

**Q: データベースエラーが発生する**
A: アプリデータをクリアして再起動してください
