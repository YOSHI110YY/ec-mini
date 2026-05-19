# ec-mini

Spring Boot / Thymeleaf / MySQL を使用して開発したECサイトです。

## URL

※ ローカル環境で動作確認

---

## 使用技術

- Java 17
- Spring Boot
- Thymeleaf
- Spring Security
- MySQL
- Bootstrap 5
- Maven
- Git / GitHub

---

## 主な機能

### ユーザー側

- 商品一覧
- 商品詳細
- 商品検索
- カート機能
- 注文機能
- ログイン / ログアウト

### 管理者側

- 商品管理
- 商品登録 / 編集 / 削除
- 商品検索
- ページング
- 注文管理
- 注文ステータス変更
- Dashboard表示
- 在庫状態表示

---

## 工夫した点

- Thymeleaf の fragment を利用し、
  header / sidebar を共通化して
  管理画面全体の UI を統一しました。

- 注文ステータス変更と Dashboard を連動させ、
  未発送件数がリアルタイムで反映されるようにしました。

- Bootstrap を利用し、
  管理画面を見やすく整理しました。

---

## 苦労した点

- Thymeleaf のレイアウト共通化で
  fragment の循環参照が発生し、
  TemplateEngine エラーの切り分けに苦労しました。

- 商品画像アップロード時の
  multipart/form-data や update 処理で
  400 エラーの修正に苦労しました。

---

## 今後追加したい機能

- レコメンド機能
- 最近見た商品
- レビュー機能
- グラフ機能
- AWS デプロイ
