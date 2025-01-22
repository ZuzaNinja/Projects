import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import DeleteDevice from './DeleteDevice';
import './Devices.css';

function Devices() {
    const [devices, setDevices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
       fetch('http://localhost:80/device/api/devices')
       //fetch('http://localhost:8081/api/devices')

            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch devices');
                }
                return response.json();
            })
            .then(data => {
                setDevices(data);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    const handleDelete = (deviceId) => {
        setDevices(devices.filter(device => device.id !== deviceId));
    };

    if (loading) {
        return <p>Loading devices...</p>;
    }

    if (error) {
        return <p>Error: {error}</p>;
    }

    return (
        <div className="devices-container">
            <h1>Device List</h1>
            <ul className="devices-list">
                {Array.isArray(devices) ? (
                    devices.map(device => (
                        <li key={device.id} className="device-item">
                            <span className="device-info">
                                {device.description} - Max Hourly Consumption: {device.maxHourlyConsumption}
                            </span>
                            <Link to={`/update-device/${device.id}`}>
                                <button className="update-button">Update</button>
                            </Link>
                            <DeleteDevice deviceId={device.id} onDelete={handleDelete} />
                        </li>
                    ))
                ) : (
                    <p>No devices available</p>
                )}
            </ul>
        </div>
    );
}

export default Devices;
