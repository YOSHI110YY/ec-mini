import { Link } from "react-router-dom";
import "./Header.css";

function Header({ cartCount }) {
    return (
        <header className="header">
            <h1 className="logo">Fit Deli</h1>

            <Link
                to="/cart"
                className="cart-link"
            >
                🛒 {cartCount}
            </Link>
        </header>
    );
}

export default Header;