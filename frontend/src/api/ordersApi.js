export const fetchOrders = async () => {
    const response = await fetch(
        "http://localhost:8080/api/orders",
        {
            method: "GET",
            credentials: "include",
        }
    );

    if (!response.ok) {
        throw new Error(
            `注文履歴の取得に失敗しました: ${response.status}`
        );
    }

    return response.json();
};

export async function fetchOrderById(id) {
    const response = await fetch(
        `http://localhost:8080/api/orders/${id}`,
        {
            credentials: "include"
        }
    );

    if (!response.ok) {
        throw new Error("注文詳細の取得に失敗しました");
    }

    return response.json();
}