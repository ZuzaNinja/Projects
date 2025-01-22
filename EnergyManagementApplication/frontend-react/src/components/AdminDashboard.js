import React, { useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function AdminDashboard() {
    const navigate = useNavigate();

    useEffect(() => {
        const role = sessionStorage.getItem('role');
        if (role !== 'admin') {
            navigate('/'); // redirect if not admin
        }
    }, [navigate]);

    return (
        <div
            style={{
                position: 'relative',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                height: '100vh',
                backgroundImage: 'url("/admin-client-dashboard.jpg")',
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundRepeat: 'no-repeat',
            }}
        >
            <div
                style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    backgroundColor: 'rgba(255, 255, 255, 0.4)',
                    zIndex: 1,
                }}
            ></div>

            <div style={{ position: 'relative', zIndex: 2, textAlign: 'center' }}>
                <h1>Admin Dashboard</h1>
                <nav>
                    <Link to="/devices">Manage Devices</Link>
                    <br />
                    <Link to="/users">Manage Users</Link>
                    <br />
                    <Link to="/user-device-mapping">Assign Device to User</Link>
                    <br />
                    <Link to="/add-device">Add New Device</Link>
                    <br />
                    <Link to="/add-user">Add New User</Link>
                    <br />
                    <Link to="/chat-with-clients">Chat with Clients</Link>
                </nav>
            </div>
        </div>
    );
}

export default AdminDashboard;
