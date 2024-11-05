import React, { useState, useEffect } from 'react';
import { useAuth } from '../common/authContext';

const Subscription = () => {
    const { user } = useAuth();
    const [email, setEmail] = useState('');
    const [loading, setLoading] = useState(false);
    const apiUrl = process.env.REACT_APP_NOTIFICATION_APP_API_URL;

    const handleSubscribe = async (e) => {
        e.preventDefault();

        if (!email.trim()) return;
        setLoading(true);
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
        } catch (error) {
            console.error('Error during subscription:', error);
        }
    };

    return (
        <div className="row" style={{ display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
           {!loading && ( <div className="input-field inline" style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <input
                    id="subscribe-email"
                    type="email"
                    placeholder="Want to subscribe?"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    style={{ marginRight: '10px' }}
                />
                <button
                    onClick={handleSubscribe}
                    className="waves-effect waves-orange btn-small"
                    disabled={loading}>
                    <i className="medium material-icons">add_reaction</i>
                </button>
            </div> )}

            {loading && (
                <div className="progress" style={{ width: '100%', marginTop: '10px' }}>
                    <div className="indeterminate"></div>
                </div>
            )}
        </div>
    );
};

export default Subscription;
