import React, { useState, useEffect, useCallback } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const WebSocketTester = () => {
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

            // Subscribe to new events
            client.subscribe('/topic/events/new', (message) => {
                const event = JSON.parse(message.body);
                setMessages(prev => ({
                    ...prev,
                    newEvents: [...prev.newEvents, event]
                }));
            });


        };

        client.onDisconnect = () => {
            setConnected(false);
        };

        client.activate();
        setStompClient(client);

        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, []);

    const clearMessages = useCallback(() => {
        setMessages({
            newEvents: [],
            ticketUpdates: [],
            salesAlerts: []
        });
    }, []);

    return (
        <div className="p-4 space-y-4 max-w-7xl mx-auto">
            {/* Connection Status and Clear Button */}
            <div className="flex items-center justify-between mb-4">
                <div className="flex items-center gap-2">
                    <div className={`w-3 h-3 rounded-full ${connected ? 'bg-green-500' : 'bg-red-500'}`} />
                    <span className="text-sm font-medium text-gray-700">
                        {connected ? 'Connected' : 'Disconnected'}
                    </span>
                </div>
                <button
                    onClick={clearMessages}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                >
                    Clear Messages
                </button>
            </div>

            {/* Cards Grid */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {/* New Events Card */}
                <div className="bg-white rounded-lg border border-gray-200 shadow">
                    <div className="px-6 py-4 border-b border-gray-200">
                        <h3 className="text-lg font-semibold text-gray-900">New Events</h3>
                    </div>
                    <div className="p-6">
                        <div className="space-y-3">
                            {messages.newEvents.map((event, index) => (
                                <div key={index} className="p-3 bg-gray-50 rounded-md">
                                    <pre className="text-sm text-gray-700 whitespace-pre-wrap">
                                        {JSON.stringify(event, null, 2)}
                                    </pre>
                                </div>
                            ))}
                            {messages.newEvents.length === 0 && (
                                <p className="text-sm text-gray-500">No new events</p>
                            )}
                        </div>
                    </div>
                </div>

                {/* Ticket Updates Card */}
                <div className="bg-white rounded-lg border border-gray-200 shadow">
                    <div className="px-6 py-4 border-b border-gray-200">
                        <h3 className="text-lg font-semibold text-gray-900">Ticket Updates</h3>
                    </div>
                    <div className="p-6">
                        <div className="space-y-3">
                            {messages.ticketUpdates.map((update, index) => (
                                <div key={index} className="p-3 bg-gray-50 rounded-md">
                                    <pre className="text-sm text-gray-700 whitespace-pre-wrap">
                                        {JSON.stringify(update, null, 2)}
                                    </pre>
                                </div>
                            ))}
                            {messages.ticketUpdates.length === 0 && (
                                <p className="text-sm text-gray-500">No ticket updates</p>
                            )}
                        </div>
                    </div>
                </div>

                {/* Sales Alerts Card */}
                <div className="bg-white rounded-lg border border-gray-200 shadow">
                    <div className="px-6 py-4 border-b border-gray-200">
                        <h3 className="text-lg font-semibold text-gray-900">Sales Alerts</h3>
                    </div>
                    <div className="p-6">
                        <div className="space-y-3">
                            {messages.salesAlerts.map((alert, index) => (
                                <div key={index} className="p-3 bg-gray-50 rounded-md">
                                    <pre className="text-sm text-gray-700 whitespace-pre-wrap">
                                        {JSON.stringify(alert, null, 2)}
                                    </pre>
                                </div>
                            ))}
                            {messages.salesAlerts.length === 0 && (
                                <p className="text-sm text-gray-500">No sales alerts</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default WebSocketTester;