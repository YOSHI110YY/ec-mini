import { Link } from "react-router-dom";
import { user } from "../data/user";
import "./MyPage.css";

function MyPage() {
    return (
        <main className="mypage page-container">
            <h1>マイページ</h1>

            <section className="mypage-section">
                <h2>会員情報</h2>

                <div className="user-info">
                    <p>
                        <span>名前</span>
                        {user.name}
                    </p>

                    <p>
                        <span>メールアドレス</span>
                        {user.email}
                    </p>
                </div>
            </section>

            <section className="mypage-section">
                <h2>メニュー</h2>

                <div className="mypage-menu">
                    <Link to="/orders" className="mypage-menu-card">
                        <strong>注文履歴</strong>
                        <span>過去の注文を確認する</span>
                    </Link>

                    <Link to="/favorites" className="mypage-menu-card">
                        <strong>お気に入り</strong>
                        <span>保存した商品を見る</span>
                    </Link>
                </div>
            </section>
        </main>
    );
}

export default MyPage;