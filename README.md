# Fit Deli

健康志向ユーザー向けの冷凍宅配弁当ECサイトです。

![Java CI with Maven](https://github.com/YOSHI110YY/ec-mini/actions/workflows/ci.yml/badge.svg)

---

## 概要

Fit Deliは、冷凍宅配弁当の商品閲覧から決済、注文履歴の確認までを行えるECサイトです。

もともとSpring BootとThymeleafで構築していた一般ユーザー画面を、React + Viteへ移行しました。

現在は、役割に応じて画面構成を分けています。

* 一般ユーザー画面：React + Vite
* ログイン画面：Thymeleaf
* 管理画面：Thymeleaf
* バックエンド：Spring Boot REST API
* 決済：Stripe Checkoutテストモード

ReactとSpring Boot間ではREST APIを使用し、Cookieベースの認証情報を共有しています。

---

## Demo

* GitHub
  https://github.com/YOSHI110YY/ec-mini

* 公開環境
  https://ec-mini-production.up.railway.app/

> Railwayの無料トライアル終了に伴い、現在は公開環境を停止しています。
> ローカル環境では、React画面・管理画面・Stripeテスト決済を含む一連の動作を確認できます。

---

## テストアカウント

ポートフォリオ確認用の簡易アカウントです。

### 一般ユーザー

```text
ID: testuser
PASS: password
```

### 管理者

```text
ID: admin
PASS: password
```

---

## 主な機能

### 一般ユーザー

* ログイン・ログアウト
* 商品一覧表示
* カテゴリによる商品絞り込み
* 商品詳細表示
* お気に入り追加・解除
* お気に入り一覧表示
* カート追加
* カート数量変更
* カート商品削除
* 注文内容確認
* Stripe Checkoutによるテスト決済
* 注文完了
* 注文完了後のカート初期化
* 注文履歴表示
* 注文詳細表示
* マイメニュー表示

### 管理者

* 管理者ログイン
* 管理ダッシュボード表示
* 商品一覧表示
* 商品登録
* 商品編集
* 商品削除
* 在庫数管理
* 注文一覧表示
* 注文詳細表示
* 注文ステータス更新

---

## システム構成

```text
Browser
  │
  ├─ React + Vite
  │    ├─ 商品一覧
  │    ├─ 商品詳細
  │    ├─ お気に入り
  │    ├─ カート
  │    ├─ 注文確認
  │    ├─ 注文完了
  │    └─ 注文履歴・注文詳細
  │
  └─ Thymeleaf
       ├─ ログイン
       └─ 管理画面
              │
              ▼
       Spring Boot
       ├─ REST API
       ├─ Spring Security
       ├─ Service
       ├─ Repository
       └─ Stripe Checkout
              │
              ▼
            MySQL
```

---

## 画面構成

| 画面              | 使用技術      | 主な役割               |
| --------------- | --------- | ------------------ |
| ログイン            | Thymeleaf | 一般ユーザー・管理者の認証      |
| 商品一覧            | React     | 商品表示、カテゴリ絞り込み      |
| 商品詳細            | React     | 商品情報表示、カート・お気に入り追加 |
| お気に入り           | React     | お気に入り商品の一覧と解除      |
| カート             | React     | 数量変更、削除、注文画面への遷移   |
| 注文確認            | React     | 注文内容と合計金額の確認       |
| Stripe Checkout | Stripe    | テスト決済              |
| 注文完了            | React     | 注文完了結果の表示          |
| 注文履歴            | React     | ログインユーザーの注文一覧      |
| 注文詳細            | React     | 注文明細の表示            |
| 管理ダッシュボード       | Thymeleaf | 商品・注文情報の確認         |
| 商品管理            | Thymeleaf | 商品登録、編集、削除、在庫管理    |
| 注文管理            | Thymeleaf | 注文確認、ステータス更新       |

---

## 使用技術

### Backend

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* Bean Validation
* Maven

### Frontend

* React
* Vite
* JavaScript
* React Router
* HTML
* CSS
* Thymeleaf
* Bootstrap 5

### Database

* MySQL 8

### Payment

* Stripe Checkout

### Infrastructure / Tools

* Docker
* Docker Compose
* Railway
* GitHub Actions
* Git
* GitHub

### Testing

* JUnit 5
* Mockito
* MockMvc

---

## React移行について

一般ユーザー向け画面を、Spring BootのThymeleafテンプレートからReactへ移行しました。

### 移行対象

* 商品一覧
* 商品詳細
* お気に入り
* カート
* 注文確認
* 注文完了
* 注文履歴
* 注文詳細
* ヘッダー
* マイメニュー

### Thymeleafを継続して使用する画面

* ログイン画面
* 管理画面

ログインと管理機能は既存のSpring SecurityおよびThymeleaf構成を活用し、一般ユーザーが利用する画面をReact化しています。

---

## REST API連携

ReactからSpring BootのREST APIを呼び出して、商品・お気に入り・カート・注文情報を取得および更新します。

主な連携内容は以下のとおりです。

* 商品一覧取得
* 商品詳細取得
* お気に入り一覧取得
* お気に入り追加・解除
* カート内容取得
* カート追加
* カート数量更新
* カート商品削除
* Stripe Checkoutセッション作成
* 注文履歴取得
* 注文詳細取得
* ログアウト

API呼び出し時には、ログイン状態を維持するためCookieを送信します。

```javascript
fetch("http://localhost:8080/api/...", {
  credentials: "include"
});
```

---

## Stripeテスト決済

Stripe Checkoutのテストモードを使用しています。

Stripe決済を利用するには、Stripeのシークレットキーを環境変数に設定します。

```text
STRIPE_SECRET_KEY=sk_test_xxxxxxxxxxxxxxxxx
```

### テストカード

```text
カード番号: 4242 4242 4242 4242
有効期限: 任意の未来日
CVC: 任意の3桁
メールアドレス: 任意
```

### 決済処理

1. Reactの注文確認画面からSpring Bootへリクエスト
2. Spring BootがStripe Checkoutセッションを作成
3. Stripe Checkout画面へ遷移
4. テスト決済を実行
5. 注文データを登録
6. 注文完了画面を表示
7. カート内容を初期化
8. 注文履歴へ反映

> 本アプリは学習用ポートフォリオのため、決済成功後のリダイレクト処理を利用して注文を確定しています。実運用では、Stripe Webhookを使用した注文確定が適しています。

---

## 例外処理

以下の独自例外を使用しています。

* `StockException`
* `OrderException`
* `PaymentException`

`GlobalExceptionHandler`を使用し、アプリケーション全体の例外処理を共通化しています。

---

## ローカル起動方法

### 必要環境

* Java 17
* Maven
* Node.js
* npm
* MySQL 8
* Stripeテスト用シークレットキー

---

### 1. リポジトリを取得

```bash
git clone https://github.com/YOSHI110YY/ec-mini.git
cd ec-mini
```

---

### 2. MySQLを起動

ローカルのMySQL、またはDocker Composeを使用してデータベースを起動します。

```bash
docker compose up -d
```

データベースの接続情報は、ローカル環境に合わせてSpring Bootの設定ファイルまたは環境変数へ設定してください。

---

### 3. Stripeの環境変数を設定

#### PowerShell

```powershell
$env:STRIPE_SECRET_KEY="sk_test_xxxxxxxxxxxxxxxxx"
```

#### コマンドプロンプト

```cmd
set STRIPE_SECRET_KEY=sk_test_xxxxxxxxxxxxxxxxx
```

---

### 4. Spring Bootを起動

プロジェクトルートで実行します。

```bash
mvn spring-boot:run
```

バックエンドは以下のURLで起動します。

```text
http://localhost:8080
```

---

### 5. Reactを起動

別のターミナルを開きます。

```bash
cd frontend
npm install
npm run dev
```

Reactは以下のURLで起動します。

```text
http://localhost:5173
```

---

### 6. ログイン

最初にSpring Boot側のログイン画面へアクセスします。

```text
http://localhost:8080/login
```

一般ユーザー：
ログイン後、http://localhost:5173 を利用します。

管理者：
http://localhost:8080/admin を利用します。

---

## Docker

MySQLをDockerで起動したい場合は、Docker Composeを利用できます。

```bash
docker compose up -d

```
停止する場合は以下を実行します。

```bash
docker compose down
```

> React開発サーバーは、`frontend`ディレクトリで別途`npm run dev`を実行してください。

---

## テスト

JUnit 5、Mockito、MockMvcを使用してテストを実装しています。

### 主なテスト対象

* ProductService
* CartService
* OrderService
* FavoriteService
* ProductController
* CartController
* OrderController
* AdminProductController

### テスト実行

```bash
mvn test
```

---

## CI

GitHub Actionsを使用し、pushおよびpull request時にMavenテストを実行します。

```text
mvn test
```

---

## 画面イメージ

### トップページ
![トップページ](docs/images/product-hero.png)

### 商品一覧
![商品一覧](docs/images/product-list.png)

### 商品詳細
![商品詳細](docs/images/product-detail.png)

### お気に入り
![お気に入り](docs/images/favorites.png)

### カート
![カート](docs/images/cart.png)

### 注文完了
![注文完了](docs/images/order-complete.png)

### 注文履歴
![注文履歴](docs/images/order-history.png)

### 管理画面
![管理画面](docs/images/admin-dashboard.png)

> 画像ファイル名が異なる場合は、`docs/images`内の実際のファイル名に合わせて修正してください。

---

## 設計・実装で意識した点

* ReactとSpring Bootの役割分離
* REST APIを使用したフロントエンド・バックエンド連携
* Controller、Service、Repositoryの責務分離
* Spring Securityによる認証・認可
* Cookieを利用したログイン状態の共有
* 独自例外によるエラーの分類
* `GlobalExceptionHandler`による例外処理の共通化
* Stripe処理のService層への分離
* 環境変数による機密情報の管理
* 在庫数を考慮した注文処理
* 注文完了後のカート初期化
* 共通API関数による重複処理の削減
* React Routerを使用した画面遷移
* コンポーネント分割による再利用性の向上

---

---

## 今後改善したい点

* Stripe Webhookによる注文確定
* AWSなどを利用した再デプロイ
* Reactのコンポーネントテスト追加
* REST APIのテスト強化
* TypeScriptへの移行
* 商品レビュー機能
* 管理画面の検索・絞り込み機能
* レスポンシブ表示のさらなる改善

---

## 補足

本アプリでは、既存のSpring BootアプリケーションへReactを追加し、一般ユーザー画面を段階的にSPAへ移行しました。

単純なCRUD機能だけでなく、認証、在庫管理、お気に入り、カート、決済、注文履歴、管理機能まで含めたECサイトとして実装しています。
