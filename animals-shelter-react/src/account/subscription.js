import React, { useState, useEffect } from 'react';
import { useAuth } from '../common/authContext';

const Subscription = () => {
    const { user } = useAuth();
    const [email, setEmail] = useState('');
    const [loading, setLoading] = useState(false);
    const apiUrl = process.env.REACT_APP_NOTIFICATION_APP_API_URL;

    useEffect(() => {
        if (user.email) {
            setEmail(user.email);
        }
    }, [user.email]);

    const handleSubscribe = async (e) => {
        e.preventDefault();

        if (!email.trim()) return;

        try {
            await fetch(`${apiUrl}/external/animal-notify-subscribe`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: email,
                    approver: user.email,
                }),
            });
            setEmail('');
            setLoading(true);
        } catch (error) {
            console.error('Error during subscription:', error);
        }
    };

    return (
        <div className="subscription-container">
            {!loading && (
                <div className="input-container quick-subscribe-input-container">
                    <input
                        id="subscribe-email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="quick-subscribe-input"
                    />
                    <button
                        onClick={handleSubscribe}
                        className="waves-effect waves-orange btn-small quick-subscribe-button"
                        disabled={loading}>
                        <i className="material-icons">add_reaction</i>
                    </button>
                </div>
            )}

            {loading && (
                <div className="progress">
                    <div className="indeterminate"></div>
                </div>
            )}
        </div>
    );
};

export default Subscription;
