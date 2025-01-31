import React from 'react';
import { useAuth } from './common/authContext';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Button } from '@nextui-org/react';

const Logout = () => {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post(`/ansh/api/auth/logout`, {}, { withCredentials: true });
            logout();
            navigate('/login');
        } catch (error) {
            console.error('Error logging out:', error);
        }
    };

    return (
        <Button color="default" onPress={handleLogout}>
            Logout
        </Button>
    );
};

export default Logout;
