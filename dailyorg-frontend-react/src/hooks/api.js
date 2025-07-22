const API_URL = import.meta.env.VITE_API_URL;

export default async function callApi(method, url, body = null, headers = {}, provideToken = true, isResponseJson = true) {
  const response = await fetch(`${API_URL}/api/${url}`, {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...provideToken ? { 'Authorization': 'Bearer ' + localStorage.getItem('token') } : {},
      ...headers,
    },
    body: body ? JSON.stringify(body) : null,
  });

  if (!response.ok) {
    throw new Error(`HTTP error! Status: ${response.status}`);
  }

  return await (isResponseJson ? response.json() : response.text());
}
