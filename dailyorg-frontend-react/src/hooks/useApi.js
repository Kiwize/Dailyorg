import { useState, useEffect } from "react";

const API_URL = import.meta.env.VITE_API_URL;

export default function useApi(method, url, body = null, headers = {}) {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchData = async (customBody = body) => {
        setLoading(true);
        try {
            const response = await fetch(`${API_URL}/api/${url}`, {
                method,
                headers: {
                    "Content-Type": "application/json",
                    ...headers
                },
                body: customBody ? JSON.stringify(customBody) : null
            });

            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);

            const result = await response.json();
            setData(result);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Auto-fetch if body is provided (for normal API calls)
    useEffect(() => {
        if (body) {
            fetchData();
        } else {
            setLoading(false);
        }
    }, []); // Runs only once on mount

    return { data, loading, error, fetchData };
}
