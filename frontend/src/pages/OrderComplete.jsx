import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { fetchCheckoutSuccess } from "../api/checkoutApi";
import "./OrderComplete.css";

function OrderComplete({ clearCart }) {
    const [searchParams] = useSearchParams();
    const sessionId = searchParams.get("session_id");

    const [order, setOrder] = useState(null);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadCompletedOrder = async () => {
            if (!sessionId) {
                setError("決済情報が見つかりませんでした。");
                return;
            }

            try {
                const data = await fetchCheckoutSuccess(sessionId);

                setOrder(data);
                clearCart();
            } catch (error) {
                console.error(error);
                setError("注文情報を取得できませんでした。");
            }
        };

        loadCompletedOrder();
    }, [sessionId]);

    if (error) {
        return (
            <main className="order-complete-page">
                <section className="order-complete-card">
                    <p>{error}</p>

                    <Link
                        to="/products"
                        className="complete-primary-link"
                    >
                        商品一覧へ戻る
                    </Link>
                </section>
            </main>
        );
    }

    if (!order) {
        return (
            <main className="order-complete-page">
                <section className="order-complete-card">
                    <p>注文情報を読み込んでいます...</p>
                </section>
            </main>
        );
    }

    return (
        <main className="order-complete-page">
            <section className="order-complete-card">
                <div className="order-complete-icon">
                    ✓
                </div>

                <h1>Thank You for Your Order</h1>

                <p className="order-complete-message">
                    ご注文が正常に完了しました。
                </p>

                <div className="order-complete-info">
                    <p>
                        <span>注文番号</span>
                        <strong>
                            #{order.id ?? order.orderId}
                        </strong>
                    </p>

                    <p>
                        <span>ステータス</span>
                        <strong>{order.status}</strong>
                    </p>

                    <p>
                        <span>合計金額</span>
                        <strong>
                            {order.totalPrice.toLocaleString()}円
                        </strong>
                    </p>
                </div>

                <div className="order-complete-actions">
                    <Link
                        to="/orders"
                        className="complete-primary-link"
                    >
                        注文履歴を見る
                    </Link>

                    <Link
                        to="/products"
                        className="complete-secondary-link"
                    >
                        商品一覧へ戻る
                    </Link>
                </div>
            </section>
        </main>
    );
}

export default OrderComplete;