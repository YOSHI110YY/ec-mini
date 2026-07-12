import { useState, useEffect } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProductList from "./pages/ProductList";
import ProductDetail from "./pages/ProductDetail";
import Header from "./components/Header";
import Cart from "./pages/Cart";
import OrderComfirm from "./pages/OrderComfirm.jsx";
import OrderComplete from "./pages/OrderComplete";
import OrderList from "./pages/OrderList";
import OrderDetail from "./pages/OrderDetail";
import MyPage from "./pages/MyPage";
import Favorites from "./pages/Favorites";

function App() {
    const [cartItems, setCartItems] = useState(() => {
        const savedCartItems = localStorage.getItem("cartItems");

        if (savedCartItems) {
            return JSON.parse(savedCartItems);
        }

        return [];
    });

    const cartCount = cartItems.length;

    useEffect(() => {
        localStorage.setItem(
            "cartItems",
            JSON.stringify(cartItems)
        );
    }, [cartItems]);

    function removeCartItem(productId) {
        const indexToRemove = cartItems.findIndex(
            item => item.id === productId
        );

        if (indexToRemove === -1) {
            return;
        }

        setCartItems(
            cartItems.filter((item, index) => index !== indexToRemove)
        );
    }
    function clearCart(){
        setCartItems([]);
    }
    function addCartItem(product) {
        console.log(product);

        const currentQuantity = cartItems.filter(
            item => item.id === product.id
        ).length;

        if (currentQuantity >= product.stock) {
            alert("在庫数を超えて追加できません");
            return;
        }

        setCartItems([
            ...cartItems,
            product
        ]);



    }

    return (
        <BrowserRouter>
            <Header cartCount={cartCount} />

            <Routes>
                <Route path="/" element={<ProductList />} />
                <Route path="/products" element={<ProductList />} />
                <Route
                    path="/products/:id"
                    element={
                        <ProductDetail
                            cartItems={cartItems}
                            addCartItem={addCartItem}
                        />
                    }
                />
                <Route
                    path="/cart"
                    element={
                        <Cart
                            cartItems={cartItems}
                            removeCartItem={removeCartItem}
                            addCartItem={addCartItem}
                            clearCart={clearCart}
                        />
                    }
                />
                <Route
                    path="/order/confirm"
                    element={
                        <OrderComfirm
                            cartItems={cartItems}
                            clearCart={clearCart}
                        />
                    }
                />
                <Route
                    path="/order/complete"
                    element={
                        <OrderComplete clearCart={clearCart} />
                    }
                />
                <Route
                    path="/orders"
                    element={<OrderList />}
                />
                <Route
                    path="/orders/:id"
                    element={<OrderDetail />}
                />
                <Route
                    path="/mypage"
                    element={<MyPage />}
                />
                <Route
                    path="/favorites"
                    element={<Favorites />}
                />

            </Routes>

        </BrowserRouter>
    );
}

export default App;