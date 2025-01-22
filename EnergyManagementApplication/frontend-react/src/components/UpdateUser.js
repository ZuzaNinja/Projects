import React, { useState, useEffect } from 'react';

function UpdateUser({ userId }) {
    const [name, setName] = useState('');
    const [address, setAddress] = useState('');
    const [age, setAge] = useState('');
    const [role, setRole] = useState('');
    const [success, setSuccess] = useState(false);

    useEffect(() => {
        //fetch(`http://localhost:8080/person/${userId}`)
        fetch(`http://localhost:80/person/person/${userId}`)
            .then(response => response.json())
            .then(data => {
                setName(data.name);
                setAddress(data.address);
                setAge(data.age);
                setRole(data.role);
            });
    }, [userId]);

    const handleSubmit = (e) => {
        e.preventDefault();
        //fetch(`http://localhost:8080/person/${userId}`, {
        fetch(`http://localhost:80/person/person/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, address, age, role }), 
        })
            .then(response => response.ok && setSuccess(true))
            .catch((error) => console.error('Error:', error));
    };

    return (
        <div>
            <h2>Update User</h2>
            <form onSubmit={handleSubmit}>
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
                    <input type="text" value={role} onChange={(e) => setRole(e.target.value)} />
                </label>
                <button type="submit">Update User</button>
            </form>
            {success && <p>User updated successfully!</p>}
        </div>
    );
}

export default UpdateUser;
