import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './UpdateDevice.css';

function UpdateDevice() {
    const { deviceId } = useParams();
    const [description, setDescription] = useState('');
    const [address, setAddress] = useState('');
    const [maxHourlyConsumption, setMaxHourlyConsumption] = useState('');
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        //fetch(`http://localhost:8081/api/devices/${deviceId}`)
            fetch(`http://localhost:80/device/api/devices/${deviceId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch device details');
                }
                return response.json();
            })
            .then(data => {
                setDescription(data.description);
                setAddress(data.address);
                setMaxHourlyConsumption(data.maxHourlyConsumption);
            })
            .catch((error) => {
                setError(error.message);
            });
    }, [deviceId]);

    const handleSubmit = (e) => {
        e.preventDefault();
        //fetch(`http://localhost:8081/api/devices/${deviceId}`, {
            fetch(`http://localhost:80/device/api/devices/${deviceId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                description,
                address,
                maxHourlyConsumption: parseFloat(maxHourlyConsumption),
            }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update the device');
                }
                setSuccess(true);
            })
            .catch((error) => {
                setError(error.message);
            });
    };

    useEffect(() => {
        if (success || error) {
            const timer = setTimeout(() => {
                setSuccess(false);
                setError(null);
            }, 3000);
            return () => clearTimeout(timer);
        }
    }, [success, error]);

    return (
        <div className="form-container">
            <h2>Update Device</h2>
            <form onSubmit={handleSubmit} className="form">
                <label>
                    Description:
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                    />
                </label>
                <label>
                    Address:
                    <input
                        type="text"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                    />
                </label>
                <label>
                    Max Hourly Consumption:
                    <input
                        type="number"
                        value={maxHourlyConsumption}
                        onChange={(e) => setMaxHourlyConsumption(e.target.value)}
                    />
                </label>
                <button type="submit">Update Device</button>
            </form>
            {success && <p className="success-message">Device updated successfully!</p>}
            {error && <p className="error-message">Error: {error}</p>}
        </div>
    );
}

export default UpdateDevice;
