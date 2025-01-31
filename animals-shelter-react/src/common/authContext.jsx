import React, { createContext, useContext, useState, useEffect, useMemo } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [user, setUser] = useState({ id: null, email: null });
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('jwt');

        if (token !== 'undefined' && token !== null && token !== '') {
            setIsAuthenticated(true);
        } else {
            setIsAuthenticated(false);
        }

        setIsLoading(false);
    }, []);

    const login = (userData, token) => {
        setIsAuthenticated(true);
        setUser(userData);
        localStorage.setItem('jwt', token);
    };

    const logout = () => {
        setIsAuthenticated(false);
        setUser({ id: null, email: null });
        localStorage.removeItem('jwt');
    };

    const contextValue = useMemo(() => ({
        isAuthenticated,
        user,
        login,
        logout,
        isLoading
    }), [isAuthenticated, user, isLoading]);

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
