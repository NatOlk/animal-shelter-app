import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Form, Input, Button, Spacer } from '@nextui-org/react';

interface RegisterResponse {
  email: string;
  message: string;
}

const RegistrationForm: React.FC = () => {
  const [identifier, setIdentifier] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const navigate = useNavigate();

  const handleSubmit = async () => {
    if (loading) return;

    setErrorMessage('');

    if (!identifier.trim()) {
      setErrorMessage('Identifier is required');
      return;
    }

    if (!validateEmail(email)) {
      setErrorMessage('Invalid email format');
      return;
    }

    if (password.length < 6) {
      setErrorMessage('Password must be at least 6 characters');
      return;
    }

    if (password !== confirmPassword) {
      setErrorMessage('Passwords do not match');
      return;
    }

    setLoading(true);

    try {
      const response = await fetch('/ansh/api/public/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          identifier,
          email,
          password,
        })
      });

      if (response.ok) {
        const data: RegisterResponse = await response.json();

        alert(`Registration successful! Welcome, ${data.email}!`);

        setIdentifier('');
        setEmail('');
        setPassword('');
        setConfirmPassword('');
        document.getElementById('identifier')?.focus();

        setTimeout(() => navigate('/login'), 1000);
      } else {
        const errorData = await response.json();
        setErrorMessage(errorData.message || 'Registration failed');
      }
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage('Registration failed');
      }
    } finally {
      setLoading(false);
    }
  };

  const validateEmail = (email: string) => {
    return /\S+@\S+\.\S+/.test(email);
  };

  return (
    <Form
      className="w-full justify-center items-center space-y-4"
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
      <div className="flex flex-col gap-4 max-w-md">
        <Input
          id="identifier"
          label="Identifier"
          placeholder="Enter your identifier"
          type="text"
          variant="bordered"
          value={identifier}
          onChange={(e) => setIdentifier(e.target.value)}
          labelPlacement="outside"
          isRequired />
        <Input
          id="email"
          label="Email"
          placeholder="Enter your email"
          type="email"
          variant="bordered"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          labelPlacement="outside"
          isRequired />
        <Input
          label="Password"
          placeholder="Enter your password"
          type="password"
          variant="bordered"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          labelPlacement="outside"
          isRequired
        />
        <Input
          label="Confirm Password"
          placeholder="Repeat your password"
          type="password"
          variant="bordered"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          labelPlacement="outside"
          isRequired />
      </div>
      <div className="flex flex-col gap-2 w-full items-center">
        <Button type="submit" color="default" variant="flat" isDisabled={loading}>
          {loading ? 'Registering...' : 'Register'}
        </Button>
        <Spacer y={2} />
        <Link
          to="/login"
          className="text-sm text-blue-500 hover:underline cursor-pointer">
          Back to login
        </Link>
      </div>
    </Form>
  );
};

export default RegistrationForm;
