import { Link } from "react-router-dom";
import "./OrderConfirm.css";
import { createCheckout } from "../api/checkoutApi";

function OrderConfirm({ cartItems }) {
    const groupedItems = cartItems.reduce((result, item) => {
        const existingItem = result.find(
            groupedItem => groupedItem.id === item.id
        );

        if (existingItem) {
            existingItem.quantity += 1;
        } else {
            result.push({
                ...item,
                quantity: 1
            });
        }

        return result;
    }, []);

    const totalPrice = groupedItems.reduce(
        (sum, item) =>
            sum + item.price * item.quantity,
        0
    );

    const handleCheckout = async () => {
        try {
            const checkoutItems = groupedItems.map(item => ({
                productId: item.id,
                quantity: item.quantity
            }));

            const data = await createCheckout(checkoutItems);

            window.location.href = data.checkoutUrl;
        } catch (error) {
            console.error(error);
            alert("決済ページの作成に失敗しました");
        }
    };

    return (
        <main className="order-confirm-page">
            <h1>Review Your Order</h1>

            <div className="order-confirm-list">
                {groupedItems.map(item => (
                    <article
                        key={item.id}
                        className="order-confirm-item"
                    >
                        <div>
                            <h2>{item.name}</h2>

                            <p>
                                単価：
                                {item.price.toLocaleString()}円
                            </p>

                            <p>数量：{item.quantity}個</p>
                        </div>

                        <p className="order-confirm-subtotal">
                            {(item.price * item.quantity)
                                .toLocaleString()}円
                        </p>
                    </article>
                ))}
            </div>

            <section className="order-confirm-summary">
                <p className="order-confirm-total">
                    Total：
                    {totalPrice.toLocaleString()}円
                </p>

                <div className="order-confirm-actions">
                    <Link
                        to="/cart"
                        className="order-back-link"
                    >
                        ← カートへ戻る
                    </Link>

                    <button
                        type="button"
                        className="order-submit-button"
                        onClick={handleCheckout}
                    >
                        決済へ進む
                    </button>
                </div>
            </section>
        </main>
    );
}

export default OrderConfirm;