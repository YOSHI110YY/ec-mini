import "./Cart.css";
import { Link } from "react-router-dom";

function Cart({ cartItems, removeCartItem, addCartItem, clearCart }) {
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
        <div className="cart-page">
            <h1>カート画面</h1>

            {cartItems.length === 0 ? (
                <p>カートは空です。</p>
            ) : (
                <>
                    {groupedItems.map((item, index) => (
                        <div className="cart-item" key={index}>
                            <h2>{item.name} × {item.quantity}</h2>
                            <p>{item.price}円</p>

                            <div className="cart-actions">
                                <button onClick={() => addCartItem(item)}>
                                    ＋
                                </button>

                                <button onClick={() => removeCartItem(item.id)}>
                                    －
                                </button>
                            </div>
                        </div>
                    ))}

                    <h2 className="cart-total">
                        合計：{totalPrice}円
                    </h2>

                    <button onClick={clearCart}>
                        カートを空にする
                    </button>

                    <Link to="/order/confirm">
                        注文確認へ進む
                    </Link>
                </>
            )}
        </div>
    );
}

export default Cart;