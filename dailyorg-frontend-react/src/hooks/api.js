const API_URL = import.meta.env.VITE_API_URL;

export default async function callApi(method, url, body = null, headers = {}, provideToken = true, isResponseJson = true, dontStringifyBody = false) {
  const response = await fetch(`${API_URL}/api/${url}`, {
    method,
    credentials: 'include',
    headers: {
      ...(dontStringifyBody ? {} : { 'Content-Type': 'application/json' }),
      ...headers,
    },
    body: body ? (method === 'GET' ? null : dontStringifyBody ? body : JSON.stringify(body)) : null,
  });

  const text = await response.text();
  let content;

  try {
    content = JSON.parse(text);
  } catch (e) {
    content = text;
  }

  return {
    status: response.status,
    content: content,
  };
}
