import { Link } from "react-router-dom";
import "./OrderComplete.css";

function OrderComplete() {
    return (
        <div className="order-complete">
            <h1>ご注文ありがとうございました！</h1>

            <p>注文が正常に完了しました。</p>

            <Link to="/products" className="back-to-products">
                商品一覧へ戻る
            </Link>
        </div>
    );
}

export default OrderComplete;