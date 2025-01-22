import React from 'react';

function DeleteUser({ userId, onDelete }) {
    const handleDelete = () => {
        //fetch(`http://localhost:8080/person/${userId}`, {
        fetch(`http://localhost:80/person/person/${userId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    onDelete(userId);
                }
            })
            .catch((error) => console.error('Error:', error));
    };

    return (
        <button onClick={handleDelete}>Delete User</button>
    );
}

export default DeleteUser;
