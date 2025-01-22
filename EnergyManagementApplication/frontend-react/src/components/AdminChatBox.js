import React, { useState, useEffect, useCallback, useRef } from "react";
import sendIcon from "./send.png";
import "./ChatWindow.css";

function AdminChatBox({ selectedClient, handleNewMessage }) {
    const [socket, setSocket] = useState(null);
    const [messagesByClient, setMessagesByClient] = useState({});
    const [input, setInput] = useState("");
    const [isTyping, setIsTyping] = useState({});
    const typingTimeoutRef = useRef(null);
    const messageQueue = useRef([]);

    const isSocketReady = () => socket && socket.readyState === WebSocket.OPEN;

    const connectWebSocket = useCallback(() => {
        if (socket) {
            console.log("Closing existing WebSocket connection.");
            socket.close();
        }

        const token = sessionStorage.getItem("token");
        if (!token) {
            console.error("Token missing. Please log in.");
            return;
        }

        const ws = new WebSocket(`ws://localhost:8083/chat?userId=admin&token=${token}`);

        ws.onopen = () => {
            console.log("WebSocket connection established for user: admin");
            setSocket(ws);
            processQueue();
        };

        ws.onmessage = (event) => {
            const message = JSON.parse(event.data);

            if (message.type === "typing") {
                setIsTyping((prev) => ({ ...prev, [message.senderId]: true }));
                clearTimeout(typingTimeoutRef.current);
                typingTimeoutRef.current = setTimeout(() => {
                    setIsTyping((prev) => ({ ...prev, [message.senderId]: false }));
                }, 3000);
            } else if (message.type === "stop-typing") {
                setIsTyping((prev) => ({ ...prev, [message.senderId]: false }));
            }

            else if (message.type === "message" && message.receiverId === "admin") {
                setMessagesByClient((prev) => ({
                    ...prev,
                    [message.senderId]: [...(prev[message.senderId] || []), message],
                }));

                if (handleNewMessage) {
                    handleNewMessage(message);
                }

                const readReceipt = {
                    type: "read-receipt",
                    senderId: "admin",
                    receiverId: message.senderId,
                    messageId: message.messageId,
                };

                if (isSocketReady()) {
                    socket.send(JSON.stringify(readReceipt));
                    console.log("Read receipt sent for messageId:", message.messageId);
                }
            }

            else if (message.type === "read-receipt" && message.receiverId === "admin") {
                setMessagesByClient((prev) => {
                    const updatedMessages = { ...prev };
                    const clientMessages = updatedMessages[message.senderId] || [];

                    updatedMessages[message.senderId] = clientMessages.map((msg) =>
                        msg.messageId === message.messageId ? { ...msg, seen: true } : msg
                    );

                    return updatedMessages;
                });
                console.log("Read receipt processed for admin messageId:", message.messageId);
            }
        };


        ws.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        ws.onclose = () => {
            console.log("WebSocket connection closed for user: admin");
        };

        setSocket(ws);
    }, [socket, selectedClient, handleNewMessage]);


    const sendMessage = (content) => {
        if (!content.trim()) return;

        const message = {
            type: "message",
            senderId: "admin",
            receiverId: selectedClient || "admin",
            messageId: `${new Date().getTime()}-${Math.random()}`,
            content,
            timestamp: new Date().toISOString(),
        };

        setMessagesByClient((prev) => ({
            ...prev,
            [selectedClient]: [...(prev[selectedClient] || []), message],
        }));

        if (isSocketReady()) {
            socket.send(JSON.stringify(message));
        } else {
            console.warn("WebSocket is not open. Queuing message.");
            messageQueue.current.push(message);
        }
        setInput("");
    };

    const processQueue = () => {
        if (isSocketReady()) {
            while (messageQueue.current.length > 0) {
                const queuedMessage = messageQueue.current.shift();
                socket.send(JSON.stringify(queuedMessage));
            }
        }
    };


    const handleInputChange = (e) => {
        setInput(e.target.value);

        if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);

        if (isSocketReady() && selectedClient) {
            const typingMessage = {
                type: "typing",
                senderId: "admin",
                receiverId: selectedClient,
            };
            socket.send(JSON.stringify(typingMessage));
            console.log("Typing notification sent to:", selectedClient);

            typingTimeoutRef.current = setTimeout(() => {
                if (socket && socket.readyState === WebSocket.OPEN) {
                    const stopTypingMessage = {
                        type: "stop-typing",
                        senderId: "admin",
                        receiverId: selectedClient,
                    };
                    socket.send(JSON.stringify(stopTypingMessage));
                    console.log("Stop typing notification sent to:", selectedClient);
                }
            }, 3000);
        }
    };


    const handleKeyPress = (e) => {
        if (e.key === "Enter") sendMessage(input);
    };

    useEffect(() => {
        if (selectedClient) {
            const lastMessage = messagesByClient[selectedClient]?.slice(-1)[0];
            if (
                lastMessage &&
                lastMessage.senderId !== "admin" &&
                !lastMessage.seen &&
                isSocketReady()
            ) {
                const readReceipt = {
                    type: "read-receipt",
                    senderId: "admin",
                    receiverId: selectedClient,
                    messageId: lastMessage.messageId,
                };
                socket.send(JSON.stringify(readReceipt));
                console.log("Read receipt sent for client:", selectedClient);

                setMessagesByClient((prev) => {
                    const updatedMessages = { ...prev };
                    updatedMessages[selectedClient] = updatedMessages[selectedClient]?.map((msg) =>
                        msg.messageId === lastMessage.messageId ? { ...msg, seen: true } : msg
                    );
                    return updatedMessages;
                });
            }
        }
    }, [selectedClient, messagesByClient, socket]);


    useEffect(() => {
        const reconnectInterval = setInterval(() => {
            if (!socket || socket.readyState === WebSocket.CLOSED) {
                console.log("WebSocket is closed. Reconnecting...");
                connectWebSocket();
            }
        }, 5000);

        return () => clearInterval(reconnectInterval);
    }, [socket]);


    const messagesToShow = messagesByClient[selectedClient] || [];
    const typingMessage = isTyping[selectedClient] ? <p>Typing...</p> : null;

    return (
        <div className="chat-window">
            <div className="messages">
                {messagesToShow.map((msg, index, arr) => (
                    <div
                        key={index}
                        className={`message ${msg.senderId === "admin" ? "sent" : "received"}`}
                    >
                        <div className="message-content">{msg.content}</div>
                        <div className="message-timestamp">
                            {new Date(msg.timestamp).toLocaleTimeString()}
                        </div>
                        {msg.senderId === "admin" &&
                            index === arr.length - 1 &&
                            msg.seen && (
                                <span className="seen-indicator">Seen</span>
                            )}

                    </div>
                ))}

                {isTyping[selectedClient] && <p className="typing-indicator">Typing...</p>}
            </div>


            <div className="input-area">
                <input
                    type="text"
                    value={input}
                    onChange={handleInputChange}
                    onKeyPress={handleKeyPress}
                    placeholder="Type a message..."
                />
                <button onClick={() => sendMessage(input)}>
                    <img src={sendIcon} alt="Send"/>
                </button>
            </div>
        </div>
    );
}

export default AdminChatBox;
