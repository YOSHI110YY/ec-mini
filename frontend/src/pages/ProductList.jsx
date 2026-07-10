import { useEffect, useState } from "react";
import ProductCard from "../components/ProductCard.jsx";
import "./ProductList.css";
import { fetchProducts } from "../api/productsApi";

function ProductList() {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        fetchProducts()
            .then(data => setProducts(data))
            .catch(error => console.error(error));
    }, []);

    return (
        <div className="product-list">
            {products
                .filter(product =>
                    [1, 2, 7, 12, 15, 17, 18, 20].includes(product.id)
                )
                .map(product => (
                    <ProductCard
                        key={product.id}
                        product={product}
                    />
                ))}
        </div>
    );
}

export default ProductList;