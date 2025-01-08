import React, { useState, useEffect, useCallback } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const WebSocketTester = ({ managerId }) => {
    const [stompClient, setStompClient] = useState(null);
    const [connected, setConnected] = useState(false);
    const [messages, setMessages] = useState({
        newEvents: [],
        ticketUpdates: [],
        salesAlerts: []
    });

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8081/ws'),
            debug: (str) => {
                console.log(str);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000
        });

        client.onConnect = () => {
            setConnected(true);
            console.log('Connected to WebSocket');

            // Subscribe to new events
            client.subscribe('/topic/events/new', (message) => {
                console.log('Received new event:', message);
                const event = JSON.parse(message.body);
                setMessages(prev => ({
                    ...prev,
                    newEvents: [...prev.newEvents, event]
                }));
            });

            // Subscribe to ticket updates
            client.subscribe('/topic/events/*/tickets', (message) => {
                console.log('Received ticket update:', message);
                const update = JSON.parse(message.body);
                setMessages(prev => ({
                    ...prev,
                    ticketUpdates: [...prev.ticketUpdates, update]
                }));
            });

            // Subscribe to manager-specific notifications if managerId is provided
            if (managerId) {
                client.subscribe(`/topic/managers/${managerId}/events/*`, (message) => {
                    console.log('Received sales alert:', message);
                    const notification = JSON.parse(message.body);
                    setMessages(prev => ({
                        ...prev,
                        salesAlerts: [...prev.salesAlerts, notification]
                    }));
                });
            }
        };

        client.onDisconnect = () => {
            setConnected(false);
            console.log('Disconnected from WebSocket');
        };

        client.onStompError = (frame) => {
            console.error('STOMP error:', frame);
        };

        client.activate();
        setStompClient(client);

        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [managerId]);

    const clearMessages = useCallback(() => {
        setMessages({
            newEvents: [],
            ticketUpdates: [],
            salesAlerts: []
        });
    }, []);

    const MessageCard = ({ title, messages, emptyMessage }) => (
        <div className="bg-white rounded-lg border border-gray-200 shadow">
            <div className="px-6 py-4 border-b border-gray-200">
                <h3 className="text-lg font-semibold text-gray-900">{title}</h3>
            </div>
            <div className="p-6">
                <div className="space-y-3">
                    {messages.length > 0 ? (
                        messages.map((msg, index) => (
                            <div key={index} className="p-3 bg-gray-50 rounded-md">
                                <pre className="text-sm text-gray-700 whitespace-pre-wrap">
                                    {JSON.stringify(msg, null, 2)}
                                </pre>
                            </div>
                        ))
                    ) : (
                        <p className="text-sm text-gray-500">{emptyMessage}</p>
                    )}
                </div>
            </div>
        </div>
    );

    return (
        <div className="p-4 space-y-4 max-w-7xl mx-auto">
            {/* Connection Status and Clear Button */}
            <div className="flex items-center justify-between mb-4">
                <div className="flex items-center gap-2">
                    <div
                        className={`w-3 h-3 rounded-full ${
                            connected ? 'bg-green-500' : 'bg-red-500'
                        }`}
                    />
                    <span className="text-sm font-medium text-gray-700">
                        {connected ? 'Connected' : 'Disconnected'}
                    </span>
                </div>
                <button
                    onClick={clearMessages}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border
                             border-gray-300 rounded-md shadow-sm hover:bg-gray-50
                             focus:outline-none focus:ring-2 focus:ring-offset-2
                             focus:ring-indigo-500"
                >
                    Clear Messages
                </button>
            </div>

            {/* Cards Grid */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <MessageCard
                    title="New Events"
                    messages={messages.newEvents}
                    emptyMessage="No new events"
                />
                <MessageCard
                    title="Ticket Updates"
                    messages={messages.ticketUpdates}
                    emptyMessage="No ticket updates"
                />
                <MessageCard
                    title="Sales Alerts"
                    messages={messages.salesAlerts}
                    emptyMessage="No sales alerts"
                />
            </div>
        </div>
    );
};

export default WebSocketTester;