import React, { useState } from 'react';

const Subscription = ({ email: initialEmail, readOnly = false }) => {
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

    const handleUnsubscribe = async () => {
        try {
            await fetch(`${apiUrl}/animal-notify-unsubscribe/${email}`, {
                method: 'POST',
            });
        } catch (error) {
            console.error('Error during unsubscription:', error);
        }
    };

    return (
        <>
            <form onSubmit={handleSubmit}>
                <div className="input-field inline">
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Please insert your email"
                        required
                        readOnly={true}
                    />
                    {!readOnly && (
                        <button type="submit" className="waves-effect waves-orange btn-small">Subscribe</button>
                    )}
                    {!readOnly && (
                        <button
                            type="button"
                            className="waves-effect waves-red btn-small"
                            onClick={handleUnsubscribe}
                        >
                            Unsubscribe
                        </button>
                    )}
                </div>
            </form>
        </>
    );
};

export default Subscription;
