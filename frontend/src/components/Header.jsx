import { Link } from "react-router-dom";
import "./Header.css";

function Header({ cartCount }) {
    async function handleLogout() {
        try {
            const response = await fetch(
                "http://localhost:8080/logout",
                {
                    method: "POST",
                    credentials: "include"
                }
            );

            if (!response.ok) {
                console.error(
                    `ログアウトに失敗しました: ${response.status}`
                );
            }
        } catch (error) {
            console.error(error);
        } finally {
            localStorage.removeItem("cartItems");

            window.location.href =
                "http://localhost:8080/login?logout";
        }
    }

    return (
        <header className="header">
            <Link
                to="/products"
                className="header-logo"
            >
                Fit Deli
            </Link>

            <nav className="header-nav">
                <Link to="/products">商品一覧</Link>
                <Link to="/mypage">マイページ</Link>
                <Link to="/favorites">お気に入り</Link>

                <Link to="/cart" className="cart-link">
                    🛒 {cartCount}
                </Link>

                <button
                    type="button"
                    className="logout-button"
                    onClick={handleLogout}
                >
                    Logout
                </button>
            </nav>
        </header>
    );
}

export default Header;