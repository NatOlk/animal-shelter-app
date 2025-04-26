import React from 'react';
import { useAuth } from './common/authContext';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Button } from '@nextui-org/react';

const Logout: React.FC = () => {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async (): Promise<void> => {
        await axios.post(`/ansh/api/public/auth/logout`, {}, { withCredentials: true });
        logout();
        navigate('/login');
    };

    return (
        <Button color="default" onPress={handleLogout}>
            Logout
        </Button>
    );
};

export default Logout;
