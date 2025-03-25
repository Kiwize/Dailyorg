const API_BASE_URL = "http://localhost:8080/api";

export default class APIService {
    static async request(endpoint, method = "GET", body = null, headers = {}) {
        try {
            const response = await fetch(`${API_BASE_URL}/${endpoint}`, {
                method,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("token"),
                    ...headers,
                },
                body: body ? JSON.stringify(body) : null,
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error("API Request Failed:", error);
            throw error;
        }
    }

    static get(endpoint, headers = {}) {
        return this.request(endpoint, "GET", null, headers);
    }

    static post(endpoint, body, headers = {}) {
        return this.request(endpoint, "POST", body, headers);
    }

    static put(endpoint, body, headers = {}) {
        return this.request(endpoint, "PUT", body, headers);
    }

    static delete(endpoint, headers = {}) {
        return this.request(endpoint, "DELETE", null, headers);
    }
}
