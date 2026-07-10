import { Link } from "react-router-dom";
import "./ProductCard.css";

function ProductCard({ product }) {
    return (
        <div className="product-card">
            <img
                src={`http://localhost:8080/uploads/product/${product.image}`}
                alt={product.name}
                className="product-image"
            />
            <h2>{product.name}</h2>
            <p>{product.price}円</p>
            <p>在庫：{product.stock}</p>

            <Link to={`/products/${product.id}`}>
                <button>詳細を見る</button>
            </Link>
        </div>
    );
}

export default ProductCard;