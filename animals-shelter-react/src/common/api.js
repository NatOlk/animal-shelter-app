const apiUrl = process.env.REACT_APP_API_URL;

const handleUnauthorized = () => {
    window.location.href = '/login';
};

export const apiFetch = async (url) => {
    try {
        const response = await fetch(`${apiUrl}${url}`, {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        });

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
