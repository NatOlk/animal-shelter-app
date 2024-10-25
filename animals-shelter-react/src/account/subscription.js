import React, { useState, useEffect } from 'react';

const Subscription = ({ email: initialEmail }) => {
    const apiUrl = process.env.REACT_APP_NOTIFICATION_APP_API_URL;
    const [email, setEmail] = useState(initialEmail || '');


    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await fetch(`${apiUrl}/animal-notify-subscribe`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/plain',
                },
                body: email,
            });
            setEmail('');
        } catch (error) {
            console.error('Error during subscription:', error);
        }
    };

    const handleUnsubscribe = async (subscriber) => {
        try {
            await fetch(`${apiUrl}/animal-notify-unsubscribe/${subscriber.email}`, {
                method: 'POST',
            });
        } catch (error) {
            console.error('Error during unsubscription:', error);
        }
    };

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
        </>
    );
};

export default Subscription;
