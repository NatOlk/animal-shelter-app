import React, { useState } from 'react';

const Subscription = ({ email: initialEmail, approver: initialApprover, readOnly = false }) => {
    const apiUrl = process.env.REACT_APP_NOTIFICATION_APP_API_URL;
    const [email, setEmail] = useState(initialEmail || '');
    const [approver, setApprover] = useState(initialApprover || '');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await fetch(`${apiUrl}/external/animal-notify-subscribe`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email,
                    approver
                }),
            });
            setEmail('');
            setApprover('');
        } catch (error) {
            console.error('Error during subscription:', error);
        }
    };

    const handleUnsubscribe = async () => {
        try {
            await fetch(`${apiUrl}/external/animal-notify-unsubscribe/${email}`, {
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
                        readOnly={readOnly}
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
