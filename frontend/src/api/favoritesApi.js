export async function fetchFavorites() {
    const response = await fetch(
        "http://localhost:8080/api/favorites",
        {
            credentials: "include"
        }
    );

    if (!response.ok) {
        throw new Error("お気に入りの取得に失敗しました");
    }

    return response.json();
}

export async function addFavorite(productId) {
    const response = await fetch(
        `http://localhost:8080/api/favorites/${productId}`,
        {
            method: "POST",
            credentials: "include"
        }
    );

    if (!response.ok) {
        throw new Error("お気に入りの追加に失敗しました");
    }
}

export async function deleteFavorite(productId) {
    const response = await fetch(
        `http://localhost:8080/api/favorites/${productId}`,
        {
            method: "DELETE",
            credentials: "include"
        }
    );

    if (!response.ok) {
        throw new Error("お気に入りの解除に失敗しました");
    }
}