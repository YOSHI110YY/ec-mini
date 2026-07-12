import "./Cart.css";
import { Link } from "react-router-dom";

function Cart({
                  cartItems,
                  removeCartItem,
                  addCartItem,
                  clearCart
              }) {
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

    return (
        <main className="cart-page">
            <h1>Shopping Cart</h1>

            {cartItems.length === 0 ? (
                <div className="cart-empty">
                    <p>カートに商品がありません。</p>

                    <Link
                        to="/products"
                        className="checkout-link"
                    >
                        商品一覧を見る
                    </Link>
                </div>
            ) : (
                <>
                    <div className="cart-list">
                        {groupedItems.map(item => (
                            <article
                                key={item.id}
                                className="cart-item"
                            >
                                <div className="cart-item-info">
                                    <h2>{item.name}</h2>

                                    <p className="cart-item-price">
                                        {item.price.toLocaleString()}円
                                    </p>
                                </div>

                                <div className="cart-quantity">
                                    <button
                                        type="button"
                                        className="quantity-button"
                                        onClick={() =>
                                            removeCartItem(item.id)
                                        }
                                    >
                                        −
                                    </button>

                                    <span className="cart-quantity-value">
                                        {item.quantity}
                                    </span>

                                    <button
                                        type="button"
                                        className="quantity-button"
                                        onClick={() =>
                                            addCartItem(item)
                                        }
                                    >
                                        ＋
                                    </button>
                                </div>

                                <strong>
                                    {(item.price * item.quantity)
                                        .toLocaleString()}円
                                </strong>
                            </article>
                        ))}
                    </div>

                    <section className="cart-summary">
                        <p className="cart-total">
                            Total：
                            {totalPrice.toLocaleString()}円
                        </p>

                        <div className="cart-actions">
                            <button
                                type="button"
                                className="clear-cart-button"
                                onClick={clearCart}
                            >
                                カートを空にする
                            </button>

                            <Link
                                to="/order/confirm"
                                className="checkout-link"
                            >
                                注文確認へ進む
                            </Link>
                        </div>
                    </section>
                </>
            )}
        </main>
    );
}

export default Cart;