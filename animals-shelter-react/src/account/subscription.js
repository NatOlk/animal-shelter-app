import React, { useState, useEffect } from 'react';
import { apiFetch } from '../common/api';

const Subscription = ({ email: initialEmail }) => {
    const [email, setEmail] = useState(initialEmail || '');
    const [subscribers, setSubscribers] = useState([]);
    const [notificationAppUrl, setNotificationAppUrl] = useState('');

    const fetchNotificationAppUrl = async () => {
        try {
            const url = await apiFetch('/api/config/notification-app');
            console.info('URL ' + url);
            setNotificationAppUrl(url);
        } catch (error) {
            console.error('Failed to load notification app URL:', error);
        }
    };

    const loadSubscribers = async () => {
        if (!notificationAppUrl) return;

        try {
            const data = await apiFetch(`${notificationAppUrl}/subscribers`, {
                cache: 'no-cache',
            });
            setSubscribers(data);
        } catch (error) {
            console.error('Failed to load subscribers:', error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!notificationAppUrl) return;

        try {
            await apiFetch(`${notificationAppUrl}/subscribe`, {
                method: 'POST',
                body: email,
            });
            setEmail('');
            loadSubscribers();
        } catch (error) {
            console.error('Error during subscription:', error);
        }
    };

    const handleUnsubscribe = async (email) => {
        if (!notificationAppUrl) return;

        try {
            await apiFetch(`${notificationAppUrl}/unsubscribe`, {
                method: 'POST',
                body: email,
            });
            loadSubscribers();
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

    useEffect(() => {
        setEmail(initialEmail);
    }, [initialEmail]);

    return (
        <>
            <form onSubmit={handleSubmit}>
            <div className="input-field col s6">
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Please insert your email"
                    required
                />
                <button type="submit" className="waves-effect waves-orange btn-small">Subscribe</button>
                </div>
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
