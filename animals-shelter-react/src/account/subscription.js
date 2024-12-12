import React, { useState, useEffect } from 'react';
import { useAuth } from '../common/authContext';
import { Input, Button, Spacer } from "@nextui-org/react";
import { Progress } from "@nextui-org/progress";

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
        setLoading(true);
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
        } catch (error) {
            console.error('Error during subscription:', error);
        }
    };

    return (
        <>
            {!loading && (
                <div className="input-field">
                    <Input
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        isReadOnly
                        className="max-w-xs"
                        variant="bordered"
                        type="email"
                    />
                    <Button
                        onClick={handleSubscribe}
                        color="default">
                        <i className="small material-icons">add_reaction</i>
                    </Button>
                </div>
            )
            }
            {loading && (
                <>
                    <Spacer y={10} />
                    <Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />
                </>
            )}
        </>
    );
};

export default Subscription;
