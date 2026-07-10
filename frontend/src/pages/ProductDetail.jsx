import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import "./ProductDetail.css";
import { fetchProductById } from "../api/productsApi";

function ProductDetail({ addCartItem }) {
    const { id } = useParams();

    const [product, setProduct] = useState(null);

    useEffect(() => {
        fetchProductById(id)
            .then(data => setProduct(data))
            .catch(error => console.error(error));
    }, [id]);

    if (!product) {
        return <p>読み込み中です...</p>;
    }

    return (
        <div className="product-detail">
            <img
                src={`http://localhost:8080/uploads/product/${product.image}`}
                alt={product.name}
                className="detail-image"
                alt={product.name}
                className="detail-image"
            />

            <div className="detail-info">
                <h1>{product.name}</h1>

                <p className="detail-price">
                    {product.price}円
                </p>

                <p>在庫：{product.stock}</p>
                <p>{product.description}</p>

                <button
                    className="add-cart-button"
                    onClick={() => addCartItem(product)}
                >
                    カートに追加
                </button>

                <Link to="/products" className="back-link">
                    一覧へ戻る
                </Link>
            </div>
        </div>
    );
}

export default ProductDetail;