const apiUrl = process.env.REACT_APP_API_URL;

const handleUnauthorized = () => {
  localStorage.removeItem('jwt');
  window.location.href = '/login';
};

export const apiFetch = async (url, options = {}) => {
  const { method = 'GET', body, headers = {} } = options;

  const token = localStorage.getItem('jwt');

  try {
    const response = await fetch(`${apiUrl}${url}`, {
      method,
      headers: {
        'Content-Type': 'application/json',
        Authorization: token ? `Bearer ${token}` : '',
        ...headers,
      },
      body: method !== 'GET' && body ? JSON.stringify(body) : undefined,
    });

    if (response.status === 401) {
      handleUnauthorized();
      throw new Error('Unauthorized');
    }

    if (response.status === 403) {
      handleUnauthorized();
      throw new Error('Unauthorized');
    }

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    }

    return response;
  } catch (error) {
    console.error('Fetch error:', error);
    throw error;
  }
};
