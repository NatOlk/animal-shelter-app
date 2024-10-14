import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './authContext';

function ProtectedRoute({ children }) {
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();
    const [isChecking, setIsChecking] = useState(true);

    useEffect(() => {
        if (!isAuthenticated) {
            navigate('/login');
        } else {
            setIsChecking(false);
        }
    }, [isAuthenticated, navigate]);

    if (isChecking) {
        return <div>Loading...</div>;
    }

    return children;
}

export default ProtectedRoute;
