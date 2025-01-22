import React from 'react';

function DeleteDevice({ deviceId, onDelete }) {
    const handleDelete = () => {
        if (window.confirm("Are you sure you want to delete this device?")) {
            //fetch(`http://localhost:8081/api/devices/${deviceId}`, {
            fetch(`http://localhost:80/device/api/devices/${deviceId}`, {
                method: 'DELETE',
            })
                .then(response => {
                    if (response.ok) {
                        onDelete(deviceId);
                    }
                })
                .catch((error) => console.error('Error:', error));
        }
    };

    return (
        <button onClick={handleDelete}>Delete Device</button>
    );
}

export default DeleteDevice;
