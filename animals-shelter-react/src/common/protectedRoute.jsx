import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './authContext';

function ProtectedRoute({ children }) {
    const { isAuthenticated, isLoading, user } = useAuth();
    const navigate = useNavigate();
    console.log('user' + user);
    useEffect(() => {
        if (!isLoading && (!isAuthenticated || !user || !user.email)) {
            navigate('/login');
        }
    }, [isAuthenticated, isLoading, user, navigate]);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    return isAuthenticated && user && user.email ? children : null;
}

export default ProtectedRoute;
