import React, { createContext, useContext, useEffect, useState, useMemo } from 'react';
import { apiFetch } from './api';
import { useAuth } from './authContext';

const ConfigContext = createContext();

export const ConfigProvider = ({ children }) => {
    const { isAuthenticated } = useAuth();
    const [config, setConfig] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchConfig = async () => {
            if (isAuthenticated) {
                try {
                    const data = await apiFetch('/api/config');
                    setConfig(data);
                } catch (error) {
                    setError(error);
                } finally {
                    setLoading(false);
                }
            } else {
                setLoading(false);
            }
        };

        fetchConfig();
    }, [isAuthenticated]);

    const contextValue = useMemo(() => ({
        ...config,
        loading,
        error,
    }), [config, loading, error]);

    return (
        <ConfigContext.Provider value={contextValue}>
            {children}
        </ConfigContext.Provider>
    );
};

export const useConfig = () => {
    return useContext(ConfigContext);
};
