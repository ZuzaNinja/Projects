import React, { useState, useEffect } from "react";
import AdminChatBox from "./AdminChatBox";
import "./ClientList.css";

function ChatInterface() {
    const [clients, setClients] = useState([]);
    const [selectedClient, setSelectedClient] = useState(null);
    const [unseenMessages, setUnseenMessages] = useState({});

    useEffect(() => {
        //fetch("http://localhost:8080/person")
        fetch("http://localhost:80/person/person")
            .then((response) => response.json())
            .then((data) => {
                const clientList = data.filter((person) => person.role === "client");
                setClients(clientList);
            })
            .catch((error) => console.error("Error loading clients:", error));
    }, []);

    const handleNewMessage = (message) => {
        if (selectedClient !== message.senderId) {
            setUnseenMessages((prev) => ({
                ...prev,
                [message.senderId]: true,
            }));
        }
    };

    const selectClient = (clientId) => {
        setSelectedClient(clientId);
        setUnseenMessages((prev) => ({ ...prev, [clientId]: false }));
    };

    return (
        <div style={{ display: "flex", height: "100vh" }}>
            <div style={{ width: "30%", backgroundColor: "#f0f0f0", overflowY: "auto" }}>
                <h2>Clients</h2>
                <ul className="client-list">
                    {clients.map((client) => (
                        <li
                            key={client.id}
                            className={`client-item ${selectedClient === client.id ? "selected" : ""}`}
                            onClick={() => selectClient(client.id)}
                        >
                            <span className="client-name">{client.name}</span>
                            {unseenMessages[client.id] && (
                                <span className="notification">New</span>
                            )}
                        </li>
                    ))}
                </ul>
            </div>
            <div style={{ flex: 1, padding: "20px" }}>
                {selectedClient ? (
                    <AdminChatBox
                        selectedClient={selectedClient}
                        handleNewMessage={handleNewMessage}
                    />
                ) : (
                    <h4>Select a client to start chatting</h4>
                )}
            </div>
        </div>
    );
}

export default ChatInterface;
