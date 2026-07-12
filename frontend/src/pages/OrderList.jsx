import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { fetchOrders } from "../api/ordersApi";
import "./OrderList.css";

function OrderList() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadOrders = async () => {
            try {
                const data = await fetchOrders();
                setOrders(data);
            } catch (error) {
                console.error(error);
                setError("注文履歴の取得に失敗しました");
            } finally {
                setLoading(false);
            }
        };

        loadOrders();
    }, []);

    if (loading) {
        return (
            <main className="page-container">
                <p>注文履歴を読み込んでいます...</p>
            </main>
        );
    }

    if (error) {
        return (
            <main className="page-container">
                <p>{error}</p>
            </main>
        );
    }

    return (
        <main className="page-container">
            <h1>注文履歴</h1>

            {orders.length === 0 ? (
                <p>注文履歴がありません。</p>
            ) : (
                <div className="orders-list">
                    {orders.map(order => (
                        <article
                            key={order.id}
                            className="order-card"
                        >
                            <p>
                                <strong>
                                    注文番号：#{order.id}
                                </strong>
                            </p>

                            <p>
                                合計金額：
                                {order.totalPrice.toLocaleString()}
                                円
                            </p>

                            <p>
                                ステータス：
                                <span className="order-status">
                                    {order.status}
                                </span>
                            </p>

                            <p>
                                注文日：
                                {new Date(order.createdAt)
                                    .toLocaleString("ja-JP")}
                            </p>

                            <Link
                                to={`/orders/${order.id}`}
                                className="order-detail-link"
                            >
                                詳細を見る
                            </Link>
                        </article>
                    ))}
                </div>
            )}
        </main>
    );
}

export default OrderList;