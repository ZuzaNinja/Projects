import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

function Login({ setRole }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            //const response = await fetch('http://localhost:8080/api/auth/login', {
            const response = await fetch('http://localhost:80/person/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            if (!response.ok) {
                throw new Error('Invalid credentials');
            }

            const data = await response.json();

            sessionStorage.setItem('token', data.token);
            sessionStorage.setItem('userId', data.userId);
            sessionStorage.setItem('role', data.role);
            sessionStorage.setItem('devices', JSON.stringify(data.devices || []));

            setRole(data.role);

            if (data.role === 'client') {
                navigate('/client/dashboard');
            } else if (data.role === 'admin') {
                navigate('/admin/dashboard');
            } else {
                alert('Unknown role; please try again.');
            }
        } catch (error) {
            console.error('Login failed:', error);
            alert('Login failed. Please check your credentials.');
        }
    };


    return (
        <div className="login-container">
            <form onSubmit={handleLogin} className="login-form">
                <label>
                    Username:
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </label>
                <label>
                    Password:
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </label>
                <button type="submit">Login</button>
            </form>
            <div className="login-image">
                <img src="/login.jpg" alt="Login illustration" />
            </div>
        </div>
    );
}

export default Login;
