import React, { useState } from 'react';
import './AddUser.css';

function AddUser() {
    const [name, setName] = useState('');
    const [address, setAddress] = useState('');
    const [age, setAge] = useState('');
    const [role, setRole] = useState('client');
    const [password, setPassword] = useState('');
    const [success, setSuccess] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log({ name, address, age, role, password });

        //fetch('http://localhost:8080/person', {
        fetch('http://localhost:80/person/person', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, address, age, role, password }),
        })
            .then(response => response.ok && setSuccess(true))
            .catch((error) => console.error('Error:', error));
    };

    return (
        <div className="add-user-container">
            <h2>Add User</h2>
            <form onSubmit={handleSubmit} className="add-user-form">
                <label>
                    Name:
                    <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
                </label>
                <label>
                    Address:
                    <input type="text" value={address} onChange={(e) => setAddress(e.target.value)} />
                </label>
                <label>
                    Age:
                    <input type="number" value={age} onChange={(e) => setAge(e.target.value)} />
                </label>
                <label>
                    Role:
                    <select value={role} onChange={(e) => setRole(e.target.value)}>
                        <option value="client">Client</option>
                        <option value="admin">Admin</option>
                    </select>
                </label>
                <label>
                    Password:
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </label>
                <button type="submit">Add User</button>
            </form>
            {success && <p>User added successfully!</p>}
        </div>
    );
}

export default AddUser;
