export const createCheckout = async (items) => {
    const response = await fetch(
        "http://localhost:8080/api/checkout",
        {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                items,
            }),
        }
    );

    if (!response.ok) {
        throw new Error(
            `決済ページの作成に失敗しました: ${response.status}`
        );
    }

    return response.json();
};

export const fetchCheckoutSuccess = async (sessionId) => {
    const response = await fetch(
        `http://localhost:8080/api/checkout/success?session_id=${sessionId}`,
        {
            method: "GET",
            credentials: "include",
        }
    );

    if (!response.ok) {
        throw new Error(
            `注文完了情報の取得に失敗しました: ${response.status}`
        );
    }

    return response.json();
};
