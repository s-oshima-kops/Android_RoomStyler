# Android Studio 日本語化 代替方法

## 🔧 方法1: Pleiades プラグイン (推奨)

### ✅ 手動インストール手順
1. **Pleiades公式サイト**にアクセス
   - URL: https://mergedoc.osdn.jp/
2. **「プラグイン・ダウンロード」**をクリック
3. **「pleiades.zip」**をダウンロード
4. zipファイルを解凍

### ✅ Android Studioへの適用
1. **Android Studioを完全終了**
2. **解凍したフォルダ**から以下をコピー:
   ```
   pleiades/
   ├── pleiades.jar
   └── setup.exe (Windows用)
   ```
3. **Android Studioインストールフォルダ**を見つける
   - 通常: `C:\Program Files\Android\Android Studio\`
4. **setup.exe を管理者権限で実行**
5. **Android Studio.exe を選択**
6. **適用**をクリック

## 🔧 方法2: 手動jarファイル配置

### ✅ プラグインフォルダに直接配置
1. **Android Studioの設定フォルダ**を開く:
   ```
   %USERPROFILE%\.AndroidStudio[バージョン]\config\plugins\
   ```
2. **pleiades.jar**をこのフォルダにコピー
3. **Android Studioを再起動**

## 🔧 方法3: 設定ファイル編集

### ✅ studio.vmoptions編集
1. **Android Studioインストールフォルダ**の**bin**フォルダを開く
2. **studio64.exe.vmoptions**を編集
3. **最後の行に追加**:
   ```
   -javaagent:C:\path\to\pleiades.jar
   ```
4. **パスを実際のpleiades.jarの場所に変更**
5. **Android Studioを再起動**

## 🎯 推奨手順

### Step 1: Pleiadesダウンロード
1. https://mergedoc.osdn.jp/ にアクセス
2. 「プラグイン・ダウンロード」をクリック
3. 最新版の pleiades.zip をダウンロード

### Step 2: 解凍と配置
1. ダウンロードしたzipファイルを解凍
2. pleiades.jar を見つける
3. 適切な場所にコピー

### Step 3: Android Studio設定
1. Android Studioを完全終了
2. 設定ファイルを編集 または setup.exe を実行
3. Android Studioを再起動

## 🚨 トラブルシューティング

### 問題1: Android Studioが起動しない
**解決策:**
1. studio64.exe.vmoptions の編集内容を元に戻す
2. プラグインフォルダから pleiades.jar を削除
3. Android Studioを再起動

### 問題2: 日本語化されない
**解決策:**
1. パスが正しいか確認
2. pleiades.jar のバージョンを確認
3. Android Studioのバージョンとの互換性を確認

### 問題3: 一部のメニューが英語のまま
**解決策:**
1. 完全に日本語化されるまで時間がかかる場合があります
2. 数回再起動してみる
3. キャッシュクリア: File → Invalidate Caches and Restart

## 📋 確認方法

### ✅ 日本語化成功の確認
- [ ] メニューバーが日本語表示
- [ ] File → ファイル
- [ ] Edit → 編集  
- [ ] View → 表示
- [ ] Tools → ツール

### ✅ 設定画面での確認
1. File → Settings (ファイル → 設定)
2. Appearance & Behavior → System Settings
3. Language で「日本語」が選択可能

## 🔄 元に戻す方法

### ✅ 日本語化を無効にする場合
1. studio64.exe.vmoptions から -javaagent行を削除
2. または plugins フォルダから pleiades.jar を削除
3. Android Studioを再起動

## 📞 サポート情報

### 公式リンク
- Pleiades公式: https://mergedoc.osdn.jp/
- Android Studio公式: https://developer.android.com/studio

### よくある質問
- Q: どのバージョンのPleiadesを使えばいい？
- A: 最新版を推奨。Android Studioのバージョンに関係なく動作します。

- Q: 商用利用は可能？
- A: Pleiadesは無料で商用利用可能です。
