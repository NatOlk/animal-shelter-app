import React, { useState, useEffect } from 'react';
import { useAuth } from '../common/authContext';
import { Input, Button, Progress, Spacer, Checkbox } from "@nextui-org/react";
import { LuSmilePlus } from "react-icons/lu";

const Subscription: React.FC = () => {
    const { user, isAdmin } = useAuth();
    const [email, setEmail] = useState<string>('');

    const [subscribeToNews, setSubscribeToNews] = useState<boolean>(true);
    const [subscribeToAnimalInfo, setSubscribeToAnimalInfo] = useState<boolean>(true);
    const [subscribeToVaccination, setSubscribeToVaccination] = useState<boolean>(false);

    const [subscriptionStatuses, setSubscriptionStatuses] = useState<Record<string, 'NONE' | 'PENDING' | 'ACTIVE'>>({
        animalShelterNews: 'NONE',
        animalInfo: 'NONE',
        vaccinationInfo: 'NONE',
    });

    useEffect(() => {
        if (user?.email) {
            setEmail(user.email);
        }
    }, [user?.email]);

    const handleSubscribe = async () => {
        if (!email.trim()) return;

        const selectedTopics = [];
        if (subscribeToNews) selectedTopics.push('animalShelterNews');
        if (subscribeToAnimalInfo) selectedTopics.push('animalInfo');
        if (subscribeToVaccination) selectedTopics.push('vaccination');

        for (const topic of selectedTopics) {
            setSubscriptionStatuses(prev => ({ ...prev, [topic]: 'PENDING' }));

            try {
                const body: any = { email, topic };
                if (isAdmin) {
                    body.approver = user?.email;
                }

                await fetch(`/ansh/notification/external/animal-notify-subscribe`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(body),
                });

                setSubscriptionStatuses(prev => ({ ...prev, [topic]: 'ACTIVE' }));
            } catch (error) {
                setSubscriptionStatuses(prev => ({ ...prev, [topic]: 'NONE' }));
            }
        }
    };

    const renderCheckbox = (label: string, topicKey: string, isSelected: boolean, setSelected: (v: boolean) => void) => (
        <Checkbox
            isSelected={isSelected}
            onValueChange={setSelected}
            color="default"
        >
            {label}
            {isSelected && subscriptionStatuses[topicKey] === 'PENDING' && (
                <Progress
                    isIndeterminate
                    aria-label={`Subscribing to ${label}...`}
                    size="sm"
                    className="mt-2"
                />
            )}
            {isSelected && subscriptionStatuses[topicKey] === 'ACTIVE' && (
                <div className="text-green-600 text-sm mt-1">✅ Subscribed</div>
            )}
        </Checkbox>
    );

    return (
        <div className="flex flex-col gap-4">
            {renderCheckbox('Subscribe to News', 'news', subscribeToNews, setSubscribeToNews)}
            {renderCheckbox('Subscribe to Animal Information', 'animalInfo', subscribeToAnimalInfo, setSubscribeToAnimalInfo)}
            {renderCheckbox('Subscribe to Vaccination Updates', 'vaccination', subscribeToVaccination, setSubscribeToVaccination)}

            <div className="flex items-center gap-3">
                <Input
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    isReadOnly
                    variant="bordered"
                    type="email"
                    className="max-w-xs"
                />
                <Button onPress={handleSubscribe} color="default">
                    <LuSmilePlus />
                </Button>
            </div>
        </div>
    );
};

export default Subscription;