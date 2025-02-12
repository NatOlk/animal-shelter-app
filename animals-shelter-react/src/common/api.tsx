import { FetchOptions } from "./types";

const handleUnauthorized = (): void => {
  localStorage.removeItem('jwt');
  window.location.href = '/login';
};

export async function apiFetch<T>(url: string, options: FetchOptions = {}): Promise<T> {
  const { method = 'GET', body, headers = {} } = options;
  const token = localStorage.getItem('jwt');

  try {
    const isFormData = body instanceof FormData;

    const response = await fetch(`/ansh/api${url}`, {
      method,
      headers: {
        ...(isFormData ? {} : { 'Content-Type': 'application/json' }),
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...headers,
      },
      body: method !== 'GET' && body ? (isFormData ? body : JSON.stringify(body)) : undefined,
    });

    if (response.status === 401 || response.status === 403) {
      handleUnauthorized();
      throw new Error('Unauthorized');
    }

    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(errorMessage || `Network response was not ok: ${response.status}`);
    }

    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
      return await response.json() as T;
    }

    return response as unknown as T;
  } catch (error) {
    console.error('Fetch error:', error);
    throw error;
  }
}
