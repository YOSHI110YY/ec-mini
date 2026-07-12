import { useEffect, useState } from "react";
import ProductCard from "../components/ProductCard.jsx";
import HeroSection from "../components/HeroSection";
import { fetchProducts } from "../api/productsApi";
import "./ProductList.css";

function ProductList() {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        fetchProducts()
            .then(data => setProducts(data))
            .catch(error => console.error(error));
    }, []);

    return (
        <main>
            <HeroSection />

            <section
                id="products"
                className="products-section page-container"
            >
                <div className="section-heading">
                    <p>OUR PICKS</p>
                    <h2>Recommended</h2>
                </div>

                <div className="product-list">
                    {products
                        .filter(product =>
                            [1, 2,14, 12, 15, 17, 18, 20]
                                .includes(product.id)
                        )
                        .map(product => (
                            <ProductCard
                                key={product.id}
                                product={product}
                            />
                        ))}
                </div>
            </section>
        </main>
    );
}

export default ProductList;