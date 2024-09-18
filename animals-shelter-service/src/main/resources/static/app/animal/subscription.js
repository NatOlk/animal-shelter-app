import React, { useState, useEffect } from 'react';

const Subscription = () => {
    const [email, setEmail] = useState('');
    const [subscribers, setSubscribers] = useState([]);
    const [notificationAppUrl, setNotificationAppUrl] = useState('');

    const fetchNotificationAppUrl = async () => {
        try {
            const response = await fetch('/api/config/notification-app');
            if (response.ok) {
                const url = await response.text();
                setNotificationAppUrl(url);
            } else {
                console.error('Failed to load notification app URL');
            }
        } catch (error) {
            console.error('Error fetching notification app URL:', error);
        }
    };

    const loadSubscribers = async () => {
        if (!notificationAppUrl) return;

        try {
            const response = await fetch(`${notificationAppUrl}/subscribers`, {
                cache: 'no-cache',
            });
            if (response.ok) {
                const data = await response.json();
                setSubscribers(data);
            } else {
                console.error('Failed to load subscribers');
            }
        } catch (error) {
            console.error('Error fetching subscribers:', error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!notificationAppUrl) return;

        try {
            const response = await fetch(`${notificationAppUrl}/subscribe`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(email),
            });
            if (response.ok) {
                setEmail('');
                loadSubscribers();
            }
        } catch (error) {
            console.error('Error during subscription:', error);
        }
    };

    const handleUnsubscribe = async (email) => {
        if (!notificationAppUrl) return;

        try {
            const response = await fetch(`${notificationAppUrl}/unsubscribe`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(email),
            });
            if (response.ok) {
                loadSubscribers();
            }
        } catch (error) {
            console.error('Error during unsubscription:', error);
        }
    };

    useEffect(() => {
        fetchNotificationAppUrl();
    }, []);

    useEffect(() => {
        if (notificationAppUrl) {
            loadSubscribers();
        }
    }, [notificationAppUrl]);

    return (
        <>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Please insert your email"
                    required
                />
                <button type="submit">Subscribe</button>
            </form>
            <div>
                {subscribers.map((subscriber, index) => (
                    <span key={index}>
                        {subscriber}
                        <button onClick={() => handleUnsubscribe(subscriber)}>Unsubscribe</button>
                        {index !== subscribers.length - 1 ? ', ' : ''}
                    </span>
                ))}
            </div>
        </>
    );
};

export default Subscription;
