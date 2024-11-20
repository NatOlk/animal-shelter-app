import React, { useState } from 'react';
import { useAuth } from './common/authContext';
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const apiUrl = process.env.REACT_APP_API_URL;
    const [identifier, setIdentifier] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();

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
                if (token === 'undefined' || token === null || token === '') {
                    navigate('/login');
                    return;
                }
                const userData = {
                    id: responseBody.user,
                    email: responseBody.email
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
        <div className="container">
            <div className="valign-wrapper">
                <h1> </h1>
            </div>
            <div>
                <form onSubmit={handleSubmit} autoComplete="off">
                    {errorMessage && <div className="input-field col s12 m8 l6 offset-m2 offset-l3">
                        <h5 className="red">{errorMessage}</h5>
                    </div>}
                    <div className="row">
                        <div className="input-field col s12 m8 l6 offset-m2 offset-l3">
                            <input
                                type="text"
                                id="identifier"
                                name="identifier"
                                className="validate"
                                value={identifier}
                                onChange={(e) => setIdentifier(e.target.value)}
                                required
                                autoComplete="off"
                            />
                        </div>
                        <div className="input-field col s12 m8 l6 offset-m2 offset-l3">
                            <input
                                type="password"
                                id="password"
                                name="password"
                                value={password}
                                className="validate"
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <div className="col s12 m8 l6 offset-m2 offset-l3">
                            <button className="btn waves-effect waves-orange" type="submit">Login</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Login;
