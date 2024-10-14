import React from 'react';
import { useAuth } from './authContext';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Logout = () => {
    const { logout } = useAuth();
    const navigate = useNavigate();
    const apiUrl = process.env.REACT_APP_API_URL;

    const handleLogout = async () => {
        try {
            console.log('Logging out...');
            await axios.post(`${apiUrl}/api/auth/logout`, {}, { withCredentials: true });
            logout();
            console.log('Logout successful, navigating to login...');
            navigate('/login');
        } catch (error) {
            console.error('Error logging out:', error);
        }
    };

    return (
        <button className="btn waves-effect waves-light" onClick={handleLogout}>
            Logout
        </button>
    );
};

export default Logout;
