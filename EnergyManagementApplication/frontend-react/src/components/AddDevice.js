import React, { useState } from 'react';
import './AddDevice.css';

function AddDevice() {
    const [description, setDescription] = useState('');
    const [address, setAddress] = useState('');
    const [maxHourlyConsumption, setMaxHourlyConsumption] = useState('');
    const [success, setSuccess] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();
        //fetch('http://localhost:8081/api/devices', {
        fetch('http://localhost:80/device/api/devices', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ description, address, maxHourlyConsumption }),
        })
            .then(response => response.ok && setSuccess(true))
            .catch((error) => console.error('Error:', error));
    };

    return (
        <div className="form-container">
            <h2>Add Device</h2>
            <form onSubmit={handleSubmit} className="form">
                <label>
                    Description:
                    <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} />
                </label>
                <label>
                    Address:
                    <input type="text" value={address} onChange={(e) => setAddress(e.target.value)} />
                </label>
                <label>
                    Max Hourly Consumption:
                    <input type="number" value={maxHourlyConsumption} onChange={(e) => setMaxHourlyConsumption(e.target.value)} />
                </label>
                <button type="submit">Add Device</button>
            </form>
            {success && <p>Device added successfully!</p>}
        </div>
    );
}

export default AddDevice;
