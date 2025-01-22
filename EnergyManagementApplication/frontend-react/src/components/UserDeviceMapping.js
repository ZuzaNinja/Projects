import React, { useState, useEffect } from 'react';
import './UserDeviceMapping.css';

function UserDeviceMapping() {
    const [users, setUsers] = useState([]);
    const [devices, setDevices] = useState([]);
    const [selectedUser, setSelectedUser] = useState('');
    const [selectedDevice, setSelectedDevice] = useState('');

    useEffect(() => {
        //fetch('http://localhost:8080/person')
        fetch('http://localhost:80/person/person')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch users');
                }
                return response.json();
            })
            .then(data => setUsers(data))
            .catch(err => console.error(err));

        //fetch('http://localhost:8081/api/devices')
        fetch('http://localhost:80/device/api/devices')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch devices');
                }
                return response.json();
            })
            .then(data => setDevices(data))
            .catch(err => console.error(err));
    }, []);

    const handleAssignDevice = () => {
        if (!selectedUser || !selectedDevice) {
            alert('Please select both a user and a device.');
            return;
        }
        console.log("Assigning device:", selectedDevice, "to user:", selectedUser);
        //fetch('http://localhost:8081/api/devices/assignDeviceToUser', {
        fetch('http://localhost:80/device/api/devices/assignDeviceToUser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: selectedUser,
                deviceId: selectedDevice,
            }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to assign device to user');
                }
                alert('Device assigned to user successfully!');
            })
            .catch(err => {
                console.error(err);
                alert('Error assigning device to user. Please try again.');
            });
    };

    return (
        <div className="mapping-container">
            <h2>Assign Device to User</h2>

            <select
                onChange={e => setSelectedUser(e.target.value)}
                value={selectedUser}
                className="mapping-select"
            >
                <option value="">Select User</option>
                {users.map(user => (
                    <option key={user.id} value={user.id}>
                        {user.name}
                    </option>
                ))}
            </select>

            <select
                onChange={e => setSelectedDevice(e.target.value)}
                value={selectedDevice}
                className="mapping-select"
            >
                <option value="">Select Device</option>
                {devices.map(device => (
                    <option key={device.id} value={device.id}>
                        {device.description}
                    </option>
                ))}
            </select>

            <button onClick={handleAssignDevice} className="assign-button">
                Assign Device
            </button>
        </div>
    );
}

export default UserDeviceMapping;
