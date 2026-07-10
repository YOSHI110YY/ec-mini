import { Link, useNavigate } from "react-router-dom";
import "./OrderConfirm.css";

function OrderComfirm({ cartItems, clearCart }) {
    const navigate = useNavigate();

    const totalPrice = cartItems.reduce(
        (sum, item) => sum + item.price,
        0
    );

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

    return (
        <div className="order-confirm">
            <h1>注文確認</h1>

            {groupedItems.map(item => (
                <div className="order-item" key={item.id}>
                    <p>
                        {item.name} × {item.quantity}
                        ：{item.price * item.quantity}円
                    </p>
                </div>
            ))}

            <h2 className="order-total">
                合計：{totalPrice}円
            </h2>

            <button
                className="order-button"

                onClick={() => {
                    clearCart();
                    navigate("/order/complete");
                }}
            >
                注文を確定する
            </button>

            <br /><br />

            <Link to="/cart">
                カートに戻る
            </Link>
        </div>
    );
}

export default OrderComfirm;