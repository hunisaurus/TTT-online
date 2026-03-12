import React, { createContext, useState, useContext, useEffect } from 'react';

const UserContext = createContext();

export const UserProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    const refreshUser = async () => {
        const token = localStorage.getItem('jwt');
        if (!token) {
            setUser(null);
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/user/me', {
                method: 'GET',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                const data = await response.json();
                setUser(data);
            }else {
                setUser(null);
                if (response.status === 403) {
                    console.log("No active session found (User not logged in yet).");
                }
            }
        } catch (error) {
            console.error("Error in refresh:", error);
        }
    };

    useEffect(() => {
        const token = localStorage.getItem('jwt');
        if (token) {
            refreshUser();
        }
    }, []);

    return (
        <UserContext.Provider value={{ user, setUser, refreshUser }}>
            {children}
        </UserContext.Provider>
    );
};

export const useUser = () => useContext(UserContext);