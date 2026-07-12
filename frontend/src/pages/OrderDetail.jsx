import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchOrderById } from "../api/ordersApi";
import "./OrderDetail.css";

function OrderDetail() {
    const { id } = useParams();

    const [order, setOrder] = useState(null);
    const [error, setError] = useState("");

    useEffect(() => {
        fetchOrderById(id)
            .then(data => setOrder(data))
            .catch(error => {
                console.error(error);
                setError("注文詳細を取得できませんでした。");
            });
    }, [id]);

    if (error) {
        return (
            <main className="order-detail-page page-container">
                <p>{error}</p>
            </main>
        );
    }

    if (!order) {
        return (
            <main className="order-detail-page page-container">
                <p>読み込み中です...</p>
            </main>
        );
    }

    return (
        <main className="order-detail-page page-container">
            <h1>注文詳細</h1>

            <section className="order-summary">
                <p>注文番号：#{order.id}</p>
                <p>
                    注文日：
                    {new Date(order.createdAt).toLocaleString("ja-JP")}
                </p>
                <p>ステータス：{order.status}</p>
            </section>

            <h2>注文商品</h2>

            <div className="order-items">
                {order.items.map(item => (
                    <article
                        key={item.id}
                        className="order-item-card"
                    >
                        <div className="order-item-info">
                            <h3>{item.productName}</h3>

                            <p>
                                単価：
                                {item.price.toLocaleString()}円
                            </p>

                            <p>数量：{item.quantity}個</p>

                            <p>
                                小計：
                                {item.subtotal.toLocaleString()}円
                            </p>
                        </div>
                    </article>
                ))}
            </div>

            <p className="order-total">
                合計：{order.totalPrice.toLocaleString()}円
            </p>
        </main>
    );
}

export default OrderDetail;