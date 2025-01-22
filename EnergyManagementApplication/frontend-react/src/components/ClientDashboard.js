import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Line, Bar } from 'react-chartjs-2';
import ClientChatBox from './ClientChatBox';
import './ClientDashboard.css';
import chatImage from './chat.jpg';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend
);

function ClientDashboard() {
    const [devices, setDevices] = useState([]);
    const [notifications, setNotifications] = useState([]);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [chartData, setChartData] = useState(null);
    const [chartType, setChartType] = useState('line');
    const [showChat, setShowChat] = useState(false);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);

    // Fetch devices from sessionStorage
    useEffect(() => {
        const role = sessionStorage.getItem('role');
        if (role !== 'client') {
            navigate('/');
            return;
        }

        const storedDevices = sessionStorage.getItem('devices');
        if (storedDevices && storedDevices !== "undefined") {
            try {
                const parsedDevices = JSON.parse(storedDevices);
                if (Array.isArray(parsedDevices) && parsedDevices.length > 0) {
                    setDevices(parsedDevices);
                } else {
                    console.warn("Parsed devices are empty or invalid:", parsedDevices);
                    setDevices([]);
                }
            } catch (error) {
                console.error('Failed to parse devices from sessionStorage:', storedDevices, error);
                setDevices([]);
            }
        } else {
            console.warn("No valid devices found in sessionStorage. Value:", storedDevices);
            setDevices([]);
        }

        setLoading(false);
    }, [navigate]);





    // WebSocket initialization
    useEffect(() => {
        const socket = new WebSocket("ws://localhost:8082/notifications");

        socket.onopen = () => {
            console.log("WebSocket connection established.");
        };

        socket.onmessage = (event) => {
            const message = event.data;
            console.log("Received WebSocket message:", message);

            try {
                const messageRegex = /Device (\w{8}-\w{4}-\w{4}-\w{4}-\w{12})/;
                const match = message.match(messageRegex);

                if (match) {
                    const deviceId = match[1];
                    const userDeviceIds = devices.map((device) => device.id);

                    if (userDeviceIds.includes(deviceId)) {
                        setNotifications((prev) => [...prev, message]);
                        console.log("Notification added for device:", deviceId);
                    } else {
                        console.log("Message ignored: not for a device assigned to the user.");
                    }
                } else {
                    console.error("Invalid message format:", message);
                }
            } catch (error) {
                console.error("Error processing WebSocket message:", error);
            }
        };

        socket.onclose = () => {
            console.log("WebSocket connection closed.");
        };

        socket.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        return () => socket.close();
    }, [devices]);

    // Fetch historical data for the selected date
    const fetchHistoricalData = () => {
        if (devices.length === 0) {
            console.error("No devices available to fetch data.");
            return;
        }

        const formattedDate = selectedDate.toISOString().split('T')[0];
        const deviceId = devices[0].id;

        fetch(`http://localhost:80/monitor/api/consumption/${deviceId}/${formattedDate}`)
            //fetch(`http://localhost:8082/api/consumption/${deviceId}/${formattedDate}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch consumption data");
                }
                return response.json();
            })
            .then((data) => {
                if (data.length === 0) {
                    console.error("No data available for the selected date.");
                    setChartData(null);
                    return;
                }

                const labels = data.map((entry) => `${entry.hour}:00`);
                const values = data.map((entry) => entry.energyValue);

                setChartData({
                    labels,
                    datasets: [
                        {
                            label: `Hourly Consumption on ${formattedDate} (kWh)`,
                            data: values,
                            backgroundColor: 'rgba(75, 192, 192, 0.2)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1,
                        },
                    ],
                });
            })
            .catch((error) => console.error("Error fetching historical data:", error));
    };

    return (
        <div className="dashboard-container">
            <h1>Your Devices</h1>
            <ul className="dashboard-links">
                {devices.length > 0 ? (
                    devices.map((device) => (
                        <li key={device.id}>
                            {device.description} - Max Consumption: {device.maxHourlyConsumption} kWh
                        </li>
                    ))
                ) : (
                    <p>No devices are associated with your account.</p>
                )}
            </ul>

            <h2>Notifications</h2>
            <ul className="notifications-list">
                {notifications.length > 0 ? (
                    notifications.map((notification, index) => (
                        <li key={index}>{notification}</li>
                    ))
                ) : (
                    <p>No notifications yet.</p>
                )}
            </ul>

            <h2>Historical Energy Consumption</h2>
            <div>
                <label>Select a day: </label>
                <DatePicker
                    selected={selectedDate}
                    onChange={(date) => setSelectedDate(date)}
                    dateFormat="yyyy-MM-dd"
                />
                <button onClick={fetchHistoricalData}>View Consumption</button>
            </div>

            {chartData && (
                <div>
                    <h3>{selectedDate.toISOString().split('T')[0]}</h3>
                    <button onClick={() => setChartType('line')}>Line Chart</button>
                    <button onClick={() => setChartType('bar')}>Bar Chart</button>

                    {chartType === 'line' ? (
                        <Line data={chartData} />
                    ) : (
                        <Bar data={chartData} />
                    )}
                </div>
            )}

            <img
                src={chatImage}
                alt="Open Chat"
                className="chat-trigger"
                onClick={() => setShowChat(!showChat)}
            />
            <div className={`chat-box ${showChat ? 'open' : ''}`}>
                {showChat && <ClientChatBox />}
            </div>
        </div>
    );
}

export default ClientDashboard;
