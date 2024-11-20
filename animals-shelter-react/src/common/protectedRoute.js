import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './authContext';

function ProtectedRoute({ children }) {
    const { isAuthenticated, isLoading } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isLoading && !isAuthenticated) {
            navigate('/login');
        }
    }, [isAuthenticated, isLoading, navigate]);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (!isAuthenticated) {
        return null;
    }
    return children;
}

export default ProtectedRoute;
