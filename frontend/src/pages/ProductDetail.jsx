import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import "./ProductDetail.css";
import { fetchProductById } from "../api/productsApi";
import { addFavorite } from "../api/favoritesApi";

function ProductDetail({ addCartItem }) {
    const { id } = useParams();

    const [product, setProduct] = useState(null);

    useEffect(() => {
        fetchProductById(id)
            .then(data => setProduct(data))
            .catch(error => console.error(error));
    }, [id]);

    async function handleAddFavorite() {
        try {
            await addFavorite(product.id);
            alert("お気に入りに追加しました");
        } catch (error) {
            console.error(error);
            alert("お気に入りに追加できませんでした");
        }
    }

    if (!product) {
        return (
            <main className="product-detail-page">
                <p>読み込み中です...</p>
            </main>
        );
    }

    return (
        <main className="product-detail-page">
            <Link
                to="/products"
                className="back-link"
            >
                ← 商品一覧へ戻る
            </Link>

            <section className="product-detail">
                <img
                    src={`http://localhost:8080/uploads/product/${product.image}`}
                    alt={product.name}
                    className="detail-image"
                />

                <div className="detail-info">
                    <h1>{product.name}</h1>

                    <p className="detail-price">
                        {product.price.toLocaleString()}円
                    </p>

                    <p>在庫：{product.stock}</p>
                    <p>{product.description}</p>

                    <div className="detail-actions">
                        <button
                            type="button"
                            className="add-cart-button"
                            onClick={() => addCartItem(product)}
                        >
                            カートに追加
                        </button>

                        <button
                            type="button"
                            className="secondary-button"
                            onClick={handleAddFavorite}
                        >
                            お気に入りに追加
                        </button>
                    </div>
                </div>
            </section>
        </main>
    );
}

export default ProductDetail;