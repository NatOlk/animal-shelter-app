import React, { useState, useEffect } from 'react';
import { useAuth } from '../common/authContext';
import { Input, Button, Progress, Spacer, Checkbox } from "@nextui-org/react";
import { LuSmilePlus } from "react-icons/lu";

const Subscription: React.FC = () => {
    const { user, isAdmin } = useAuth();
    const [email, setEmail] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false);

    const [subscribeToNews, setSubscribeToNews] = useState<boolean>(true);
    const [subscribeToAnimalInfo, setSubscribeToAnimalInfo] = useState<boolean>(true);
    const [subscribeToVaccination, setSubscribeToVaccination] = useState<boolean>(false);

    useEffect(() => {
        if (user?.email) {
            setEmail(user.email);
        }
    }, [user?.email]);

    const handleSubscribe = async () => {
        if (!email.trim()) return;
        setLoading(true);

        const body: any = {
            email,
            subscribeToNews,
            subscribeToAnimalInfo,
            subscribeToVaccination,
        };

        if (isAdmin) {
            body.approver = user?.email;
        }

        await fetch(`/ansh/notification/external/animal-notify-subscribe`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        });
    };

    return (
        <>
            {!loading ? (
                <div className="flex flex-col gap-4">
                    <Checkbox
                        isSelected={subscribeToNews}
                        onValueChange={setSubscribeToNews}
                        color="default">
                        Subscribe to News
                    </Checkbox>

                    <Checkbox
                        isSelected={subscribeToAnimalInfo}
                        onValueChange={setSubscribeToAnimalInfo}
                        color="default">
                        Subscribe to Animal Information
                    </Checkbox>

                    <Checkbox
                        isSelected={subscribeToVaccination}
                        onValueChange={setSubscribeToVaccination}
                        color="default">
                        Subscribe to Vaccination Updates
                    </Checkbox>

                    <div className="flex items-center gap-3">
                        <Input
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            isReadOnly
                            variant="bordered"
                            type="email"
                            className="max-w-xs"/>
                        <Button onPress={handleSubscribe} color="default">
                            <LuSmilePlus />
                        </Button>
                    </div>
                </div>
            ) : (
                <>
                    <Spacer y={1} />
                    <Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />
                </>
            )}
        </>
    );
};

export default Subscription;
