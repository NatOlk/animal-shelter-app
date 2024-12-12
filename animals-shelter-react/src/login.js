import React, { useState } from 'react';
import { useAuth } from './common/authContext';
import { useNavigate } from 'react-router-dom';
import { Form, Input, Button } from '@nextui-org/react';
import { Spacer } from "@nextui-org/react";

const Login = () => {
    const apiUrl = process.env.REACT_APP_API_URL;
    const [identifier, setIdentifier] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async () => {
        const params = new URLSearchParams();
        params.append('identifier', identifier);
        params.append('password', password);

        try {
            const response = await fetch(`${apiUrl}/api/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: params.toString(),
                credentials: 'include',
            });

            if (response.ok) {
                const responseBody = await response.json();
                const token = responseBody.token;
                if (!token) {
                    navigate('/login');
                    return;
                }
                const userData = {
                    id: responseBody.user,
                    email: responseBody.email,
                };
                login(userData, token);
                navigate('/');
            } else {
                setErrorMessage('Login failed');
            }
        } catch (error) {
            setErrorMessage('Login failed');
        }
    };

    return (
        <Form className="w-full justify-center items-center w-full space-y-4"
            validationBehavior="native"
            onSubmit={(e) => {
                e.preventDefault();
                handleSubmit();
            }}
            autoComplete="off">
            {errorMessage && (
                <div style={{ color: 'red', marginBottom: '1rem' }}>
                    {errorMessage}
                </div>
            )}
            <Spacer x={4} />
            <div className="flex flex-col gap-4 max-w-md">
                <Input
                    label="Username or Email"
                    placeholder="Enter your identifier"
                    type="text"
                    value={identifier}
                    onChange={(e) => setIdentifier(e.target.value)}
                    labelPlacement="outside"
                    isRequired
                />
                <Input
                    label="Password"
                    placeholder="Enter your password"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    labelPlacement="outside"
                    isRequired
                />
            </div>
            <div className="flex gap-2">
                <Button
                    type="submit"
                    color="default"
                    variant="flat">
                    Login
                </Button>
            </div>
        </Form>
    );
};

export default Login;
