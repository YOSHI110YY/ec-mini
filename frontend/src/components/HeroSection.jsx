import "./HeroSection.css";

function HeroSection() {
    return (
        <section className="hero-section">
            <div className="hero-content">
                <p className="hero-eyebrow">
                    FUEL YOUR DAY, THE HEALTHY WAY.
                </p>

                <h1>
                    Healthy Meals.
                    <br />
                    <span>Delivered.</span>
                </h1>

                <p className="hero-description">
                    世界のヘルシーな食文化をヒントに、
                    <br />
                    栄養バランスにこだわった冷凍ミールをお届けします。
                </p>

                <a href="#products" className="hero-button">
                    商品を見る
                </a>

                <div className="hero-features">
                    <span>Balanced Nutrition</span>
                    <span>Frozen Fresh</span>
                    <span>Global Inspiration</span>
                </div>
            </div>

            <div className="hero-visual">
                <img
                    src="http://localhost:8080/uploads/product/salmon-grill-plate.jpg"
                    alt="栄養バランスに配慮したFit Deliの食事"
                />
            </div>
        </section>
    );
}

export default HeroSection;