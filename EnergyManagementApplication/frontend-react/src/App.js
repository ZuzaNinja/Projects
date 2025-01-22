import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import AdminDashboard from './components/AdminDashboard';
import ClientDashboard from './components/ClientDashboard';
import Devices from './components/Devices';
import Users from './components/Users';
import UserDeviceMapping from './components/UserDeviceMapping';
import AddDevice from './components/AddDevice';
import AddUser from './components/AddUser';
import UpdateUser from './components/UpdateUser';
import UpdateDevice from './components/UpdateDevice';
import ChatInterface from "./components/ChatInterface";


function App() {
    const [role, setRole] = useState(sessionStorage.getItem('role'));
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        const storedRole = sessionStorage.getItem('role');
        setRole(storedRole);
    }, []);

    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<Login setRole={setRole} />} />
                    <Route
                        path="/client/dashboard"
                        element={role === 'client' ? <ClientDashboard /> : <Navigate to="/login" />}
                    />
                    <Route
                        path="/admin/dashboard"
                        element={
                            role === 'admin' ? (
                                <AdminDashboard notifications={notifications} />
                            ) : (
                                <Navigate to="/login" />
                            )
                        }
                    />
                    <Route
                        path="/devices"
                        element={role === 'admin' ? <Devices /> : <Navigate to="/login" />}
                    />
                    <Route
                        path="/users"
                        element={role === 'admin' ? <Users /> : <Navigate to="/login" />}
                    />
                    <Route
                        path="/user-device-mapping"
                        element={role === 'admin' ? <UserDeviceMapping /> : <Navigate to="/login" />}
                    />
                    <Route
                        path="/add-device"
                        element={role === 'admin' ? <AddDevice /> : <Navigate to="/login" />}
                    />
                    <Route
                        path="/add-user"
                        element={role === 'admin' ? <AddUser /> : <Navigate to="/login" />}
                    />
                    <Route
                        path="/update-user/:userId"
                        element={role === 'admin' ? <UpdateUser /> : <Navigate to="/login" />}
                    />
                    <Route
                        path="/update-device/:deviceId"
                        element={role === 'admin' ? <UpdateDevice /> : <Navigate to="/login" />}
                    />
                    <Route
                        path="/chat-with-clients"
                        element={<ChatInterface />}
                    />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
