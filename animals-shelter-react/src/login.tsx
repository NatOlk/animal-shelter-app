import React, { useState } from 'react';
import { useAuth } from './common/authContext';
import { useNavigate, Link } from 'react-router-dom';
import { Form, Input, Button, Spacer } from '@nextui-org/react';

const Login: React.FC = () => {
    const [identifier, setIdentifier] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [errorMessage, setErrorMessage] = useState<string>('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async () => {
        const params = new URLSearchParams();
        params.append('identifier', identifier);
        params.append('password', password);

        try {
            const response = await fetch(`/ansh/api/public/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: params.toString(),
                credentials: 'include',
            });

            if (response.ok) {
                const responseBody: { token: string; email: string; name: string } = await response.json();
                const token = responseBody.token;
                if (!token) {
                    navigate('/login');
                    return;
                }
                const userData = {
                    name: responseBody.name,
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
        <Form
            className="w-full justify-center items-center space-y-4"
            validationBehavior="native"
            onSubmit={(e: React.FormEvent<HTMLFormElement>) => {
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
                    variant="bordered"
                    value={identifier}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => setIdentifier(e.target.value)}
                    labelPlacement="outside"
                    isRequired />
                <Input
                    label="Password"
                    placeholder="Enter your password"
                    type="password"
                    variant="bordered"
                    value={password}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
                    labelPlacement="outside"
                    isRequired />
            </div>
            <div className="flex flex-col gap-2 w-full items-center">
                <Button type="submit" color="default" variant="flat">
                    Login
                </Button>
                <Spacer y={2} />
                <Link
                    to="/register"
                    className="text-sm text-blue-500 hover:underline cursor-pointer">
                    Not registered?
                </Link>
            </div>
        </Form >
    );
};

export default Login;
