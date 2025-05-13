import React, { createContext, useContext, useEffect, useState, useMemo, ReactNode } from 'react';
import { apiFetch } from './api';
import { useAuth } from './authContext';
import { Config, ConfigContextType } from "./types";

const ConfigContext = createContext<ConfigContextType | null>(null);

export const ConfigProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const { isAuthenticated } = useAuth();
    const [config, setConfig] = useState<Config | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<Error | null>(null);

    useEffect(() => {
        const fetchConfig = async () => {
            if (isAuthenticated) {
                try {
                    const data = await apiFetch('/api/config');
                    setConfig(data);
                } catch (err) {
                    setError(err as Error);
                } finally {
                    setLoading(false);
                }
            } else {
                setLoading(false);
            }
        };

        fetchConfig();
    }, [isAuthenticated]);

    const contextValue: ConfigContextType = useMemo(() => ({
        ...(config || { species: [], colors: [], genders: [] }),
        loading,
        error,
    }), [config, loading, error]);

    return (
        <ConfigContext.Provider value={contextValue}>
            {children}
        </ConfigContext.Provider>
    );
};

export const useConfig = (): ConfigContextType => {
    const context = useContext(ConfigContext);
    if (!context) {
        throw new Error("useConfig must be used within a ConfigProvider");
    }
    return context;
};
