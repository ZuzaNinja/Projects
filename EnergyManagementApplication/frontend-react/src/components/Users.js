import React, { useState, useEffect } from 'react';
import './Users.css';

function Users() {
    const [users, setUsers] = useState([]);
    const [editUserId, setEditUserId] = useState(null);
    const [name, setName] = useState('');
    const [address, setAddress] = useState('');
    const [age, setAge] = useState('');
    const [role, setRole] = useState('client');
    const [success, setSuccess] = useState(false);

    useEffect(() => {
        //fetch('http://localhost:8080/person')
        fetch('http://localhost:80/person/person')
            .then(response => response.json())
            .then(data => setUsers(data));
    }, []);

    const handleEdit = (user) => {
        setEditUserId(user.id);
        setName(user.name);
        setAddress(user.address);
        setAge(user.age);
        setRole(user.role);
    };

    const handleUpdate = (e) => {
        e.preventDefault();
        //fetch(`http://localhost:8080/person/${editUserId}`, {
        fetch(`http://localhost:80/person/person/${editUserId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, address, age, role }),
        })
            .then(() => {
                setUsers(users.map(user => user.id === editUserId ? { ...user, name, address, age, role } : user));
                setEditUserId(null);
                setName('');
                setAddress('');
                setAge('');
                setRole('client');
                setSuccess(true);
            });
    };

    const handleDelete = (id) => {
        //fetch(`http://localhost:8080/person/${id}`, { method: 'DELETE' })
        fetch(`http://localhost:80/person/person/${id}`, { method: 'DELETE' })
            .then(() => setUsers(users.filter(user => user.id !== id)));
    };

    return (
        <div className="users-container">
            <h1>User List</h1>
            <ul className="user-list">
                {users.map(user => (
                    <li key={user.id} className="user-item">
                        <span>{user.name} - {user.role}</span>
                        <button onClick={() => handleEdit(user)} className="edit-button">Edit</button>
                        <button onClick={() => handleDelete(user.id)} className="delete-button">Delete</button>
                    </li>
                ))}
            </ul>

            {editUserId && (
                <form onSubmit={handleUpdate} className="edit-form">
                    <label>
                        Name:
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
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
                        Age:
                        <input
                            type="number"
                            value={age}
                            onChange={(e) => setAge(e.target.value)}
                        />
                    </label>
                    <label>
                        Role:
                        <select value={role} onChange={(e) => setRole(e.target.value)}>
                            <option value="admin">Admin</option>
                            <option value="client">Client</option>
                        </select>
                    </label>
                    <button type="submit">Update User</button>
                </form>
            )}

            {success && <p className="success-message">User updated successfully!</p>}
        </div>
    );
}

export default Users;
