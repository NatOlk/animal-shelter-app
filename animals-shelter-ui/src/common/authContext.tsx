import React, { createContext, useContext, useState, useEffect, useMemo } from "react";
import { User, AuthContextType, ChildrenProps } from "./types";

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<ChildrenProps> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [user, setUser] = useState<User>({ id: null, name: null, email: null, roles: null });
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        const token = localStorage.getItem('jwt');

        setIsAuthenticated(!!token);
        setIsLoading(false);
    }, []);

    const login = (userData: User, token: string): void => {
        setIsAuthenticated(true);
        setUser(userData);
        localStorage.setItem('jwt', token);
    };

    const logout = (): void => {
        setIsAuthenticated(false);
        setUser({ id: null, name: null, email: null });
        localStorage.removeItem('jwt');
    };

    const isAdmin = useMemo(() => {
        return user?.roles?.includes("ADMIN") || false;
    }, [user]);

    const contextValue = useMemo(
        () => ({
          isAuthenticated,
          user,
          setUser,
          login,
          logout,
          isLoading,
          isAdmin,
        }),
        [isAuthenticated, user, isLoading, isAdmin]
      );


    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};
