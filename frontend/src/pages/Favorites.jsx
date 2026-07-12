import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
    fetchFavorites,
    deleteFavorite
} from "../api/favoritesApi";
import "./Favorites.css";

function Favorites() {
    const [favorites, setFavorites] = useState([]);

    useEffect(() => {
        fetchFavorites()
            .then(data => setFavorites(data))
            .catch(error => console.error(error));
    }, []);

    async function handleDeleteFavorite(productId) {
        try {
            await deleteFavorite(productId);

            setFavorites(currentFavorites =>
                currentFavorites.filter(
                    product => product.id !== productId
                )
            );
        } catch (error) {
            console.error(error);
            alert("お気に入りを解除できませんでした");
        }
    }

    return (
        <main className="favorites-page page-container">
            <h1>お気に入り一覧</h1>

            {favorites.length === 0 ? (
                <div className="favorites-empty">
                    <p>お気に入り商品はありません。</p>

                    <Link to="/products">
                        商品一覧を見る
                    </Link>
                </div>
            ) : (
                <div className="favorites-grid">
                    {favorites.map(product => (
                        <article
                            key={product.id}
                            className="favorite-card"
                        >
                            <Link to={`/products/${product.id}`}>
                                <img
                                    src={`http://localhost:8080/uploads/product/${product.image}`}
                                    alt={product.name}
                                    className="favorite-image"
                                />
                            </Link>

                            <div className="favorite-info">
                                <h2>{product.name}</h2>

                                <p className="favorite-price">
                                    {product.price.toLocaleString()}円
                                </p>

                                <div className="favorite-actions">
                                    <Link
                                        to={`/products/${product.id}`}
                                        className="favorite-detail-link"
                                    >
                                        詳細を見る
                                    </Link>

                                    <button
                                        type="button"
                                        className="danger-button"
                                        onClick={() => handleDeleteFavorite(product.id)}
                                    >
                                        お気に入り解除
                                    </button>

                                </div>
                            </div>
                        </article>
                    ))}
                </div>
            )}
        </main>
    );
}

export default Favorites;