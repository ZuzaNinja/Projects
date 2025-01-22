import React, { useState, useEffect, useRef } from 'react';
import './ClientChatBox.css';

function ClientChatBox({ initialUserId }) {
    const [userId, setUserId] = useState(initialUserId || sessionStorage.getItem('userId'));
    const [socket, setSocket] = useState(null);
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    const [loading, setLoading] = useState(true);
    const [adminTyping, setAdminTyping] = useState(false);
    const typingTimeoutRef = useRef(null);
    const messageQueue = useRef([]);

    const isSocketReady = () => socket && socket.readyState === WebSocket.OPEN;

    const connectWebSocket = () => {
        const token = sessionStorage.getItem("token");
        if (!token) {
            console.error("Token missing.");
            return;
        }

        const newSocket = new WebSocket(`ws://localhost:8083/chat?userId=${userId}&token=${token}`);
        setSocket(newSocket);

        newSocket.onopen = () => {
            console.log("WebSocket connected.");
            setLoading(false);
            processQueue();
        };

        newSocket.onmessage = (event) => {
            const message = JSON.parse(event.data);

            if (message.type === "read-receipt" && message.receiverId === userId) {
                setMessages((prev) =>
                    prev.map((msg) =>
                        msg.messageId === message.messageId ? { ...msg, seen: true } : msg
                    )
                );
                console.log("Read receipt processed for:", message.messageId);
            }

            else if (message.type === "message") {
                setMessages((prev) => [...prev, { ...message, isSent: message.senderId === userId }]);
            }

            else if (message.type === "typing" && message.senderId === "admin") {
                setAdminTyping(true);
                clearTimeout(typingTimeoutRef.current);
                typingTimeoutRef.current = setTimeout(() => {
                    setAdminTyping(false);
                }, 3000);
            } else if (message.type === "stop-typing" && message.senderId === "admin") {
                setAdminTyping(false);
            }

            console.log("Message received:", message);
        };



        newSocket.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        newSocket.onclose = () => {
            console.log("WebSocket disconnected.");
        };
    };

    const processQueue = () => {
        if (isSocketReady()) {
            while (messageQueue.current.length > 0) {
                const queuedMessage = messageQueue.current.shift();
                socket.send(JSON.stringify(queuedMessage));
            }
        }
    };

    useEffect(() => {
        const reconnectInterval = setInterval(() => {
            if (!socket || socket.readyState === WebSocket.CLOSED) {
                console.log("WebSocket is closed. Reconnecting...");
                connectWebSocket();
            }
        }, 5000);

        return () => clearInterval(reconnectInterval);
    }, [socket]);

    useEffect(() => {
        connectWebSocket();
    }, [userId]);

    useEffect(() => {
        const unreadMessages = messages.filter((msg) => !msg.seen && !msg.isSent);
        if (unreadMessages.length > 0 && isSocketReady()) {
            const lastMessage = unreadMessages[unreadMessages.length - 1];
            const readReceipt = {
                type: "read-receipt",
                senderId: userId,
                receiverId: "admin",
                messageId: lastMessage.messageId,
            };
            socket.send(JSON.stringify(readReceipt));
            console.log("Read receipt sent for:", lastMessage.messageId);

            setMessages((prev) =>
                prev.map((msg) =>
                    msg.messageId === lastMessage.messageId ? { ...msg, seen: true } : msg
                )
            );
        }
    }, [messages, socket, userId]);

    const handleSend = () => {
        if (input.trim() && isSocketReady()) {
            const message = {
                senderId: userId,
                receiverId: "admin",
                type: "message",
                content: input,
                timestamp: new Date().toISOString(),
                messageId: `${new Date().getTime()}-${Math.random()}`,
            };

            socket.send(JSON.stringify(message));
            setMessages((prev) => [...prev, { ...message, isSent: true }]);
            setInput('');
        }
    };

    const handleInputChange = (e) => {
        setInput(e.target.value);

        if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);

        if (isSocketReady()) {
            const typingMessage = {
                type: "typing",
                senderId: userId,
                receiverId: "admin",
            };
            socket.send(JSON.stringify(typingMessage));
            console.log("Typing notification sent");

            typingTimeoutRef.current = setTimeout(() => {
                if (socket && socket.readyState === WebSocket.OPEN) {
                    const stopTypingMessage = {
                        type: "stop-typing",
                        senderId: userId,
                        receiverId: "admin",
                    };
                    socket.send(JSON.stringify(stopTypingMessage));
                    console.log("Stop typing notification sent");
                }
            }, 3000);
        }
    };


    const handleKeyPress = (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            handleSend();
        }
    };

    if (loading) {
        return <p style={{ color: "green" }}>Connecting to chat...</p>;
    }

    if (!userId) {
        return <p>Loading user data...</p>;
    }

    return (
        <div className="chat-box">
            <div className="messages">
                {messages.map((msg, index) => (
                    <div key={index} className={`message ${msg.isSent ? "sent" : "received"}`}>
                        <div className="message-content">{msg.content}</div>
                        <div className="message-timestamp">
                            {new Date(msg.timestamp).toLocaleTimeString()}
                        </div>
                        {msg.isSent && index === messages.length - 1 && msg.seen && (
                            <span className="seen-indicator">Seen</span>
                        )}
                    </div>
                ))}
                {adminTyping && <p className="typing-indicator">Typing...</p>}
            </div>

            <div className="input-area">
                <input
                    type="text"
                    placeholder="Type a message..."
                    value={input}
                    onChange={handleInputChange}
                    onKeyPress={handleKeyPress}
                />
                <button onClick={handleSend} className="send-button">Send</button>
            </div>
        </div>
    );
}

export default ClientChatBox;
