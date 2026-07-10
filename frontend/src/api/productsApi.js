const API_BASE_URL = "http://localhost:8080/api/products";

export async function fetchProducts() {
    const response = await fetch(API_BASE_URL);

    if (!response.ok) {
        throw new Error("商品一覧の取得に失敗しました");
    }

    return response.json();
}

export async function fetchProductById(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`);

    if (!response.ok) {
        throw new Error("商品詳細の取得に失敗しました");
    }

    return response.json();
}