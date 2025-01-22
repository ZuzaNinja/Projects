import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Home.css';

function Home() {
    const navigate = useNavigate();

    useEffect(() => {
        const role = sessionStorage.getItem('role');
        if (role === 'client') {
            navigate('/client/dashboard');
        } else if (role === 'admin') {
            navigate('/admin/dashboard');
        }
    }, [navigate]);

    return (
        <div
            style={{
                backgroundImage: 'url("/background.jpg")',
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundRepeat: 'no-repeat',
                height: '100vh',
                display: 'flex',
                justifyContent: 'flex-start',
                alignItems: 'center',
                flexDirection: 'column',
                paddingTop: '50px',
            }}
        >
            <h1>Welcome to the Energy Management System</h1>
            <Link to="/login">
                <button style={{ marginTop: '20px' }}>Login</button>
            </Link>
        </div>
    );
}

export default Home;
