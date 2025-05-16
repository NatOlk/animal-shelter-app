import React, { useEffect, useState } from 'react';
import { useAuth } from '../common/authContext';
import { Button, Spacer, Tooltip, Popover, PopoverTrigger, PopoverContent } from "@nextui-org/react";
import { LuSmilePlus } from "react-icons/lu";
import { HiOutlineUserRemove } from "react-icons/hi";
import { topics, TopicKey } from '../common/types';
import { apiFetch } from '../common/api';
import TopicSubscriptionStatus from './topicSubscriptionStatus';

const Subscription: React.FC = () => {
  const { user, isAdmin } = useAuth();
  const [statuses, setStatuses] = useState<Record<TopicKey, string>>({});
  const [errorMessage, setErrorMessage] = useState<string>("");
  const [errorTopic, setErrorTopic] = useState<TopicKey | null>(null);

  const fetchStatuses = async () => {
    if (!user?.email) return;

    const data = await apiFetch("/api/subscription/statuses", {
      method: "POST",
      body: { approver: user.email }
    });

    if (data) {
      setStatuses({
        animalShelterNewsTopicId: data.animalShelterNewsTopicId,
        animalTopicId: data.animalTopicId,
        vaccinationTopicId: data.vaccinationTopicId,
      });
    }
  };

  useEffect(() => {
    fetchStatuses();
  }, [user]);

  const showError = (msg: string, topic: TopicKey) => {
    setErrorMessage(msg);
    setErrorTopic(topic);
  };

  const clearError = () => {
    setErrorMessage("");
    setErrorTopic(null);
  };

  const handleSubscribe = async (topicKey: TopicKey) => {
    if (!user?.email) return;

    try {
      const result = await apiFetch(`/api/subscription/register`, {
        method: 'POST',
        body: {
          email: user.email,
          topic: topicKey,
          approver: isAdmin ? user.email : undefined,
        },
      });

      if (!result) {
        showError("Failed to subscribe. Please try again.", topicKey);
        return;
      }

      await fetchStatuses();
      clearError();
    } catch {
      showError("Failed to subscribe. Please try again.", topicKey);
    }
  };

  const handleUnsubscribe = async (topicKey: TopicKey) => {
    try {
      const result = await apiFetch(`/api/subscription/unsubscribe`, {
        method: 'POST',
        body: {
          email: user?.email,
          topic: topicKey,
          approver: user?.email,
        },
      });

      if (!result) {
        showError("Failed to unsubscribe. Please try again.", topicKey);
        return;
      }

      await fetchStatuses();
      clearError();
    } catch {
      showError("Failed to unsubscribe. Please try again.", topicKey);
    }
  };

  return (
    <div className="w-full max-w-xl">
      <h2 className="text-lg font-semibold mb-4">Available Subscriptions</h2>
      <table className="w-full table-auto border-collapse">
        <thead>
          <tr className="text-left">
            <th className="py-2 px-4">Subscription</th>
            <th className="py-2 px-4">Description</th>
            <th className="py-2 px-4">Status</th>
            <th className="py-2 px-4">Actions</th>
          </tr>
        </thead>
        <tbody>
          {topics.map(({ key, label, description }) => (
            <tr key={key} className="border-t">
              <td className="py-3 px-4 font-medium">{label}</td>
              <td className="py-3 px-4">{description}</td>
              <td className="py-3 px-4">
                <TopicSubscriptionStatus status={statuses[key]} />
                {errorTopic === key && errorMessage && (
                  <Popover isOpen onOpenChange={clearError}>
                    <PopoverContent>
                      <p>{errorMessage}</p>
                    </PopoverContent>
                  </Popover>
                )}
              </td>
              <td className="py-3 px-4 flex gap-2">
                <Tooltip content="Subscribe" placement="bottom">
                  <Button onPress={() => handleSubscribe(key)}
                    color="default" variant="light"
                    className="p-2 min-w-2 h-auto">
                    <LuSmilePlus />
                  </Button>
                </Tooltip>
                <Tooltip content="Unsubscribe" placement="bottom">
                  <Button onPress={() => handleUnsubscribe(key)}
                    color="default" variant="light"
                    className="p-2 min-w-2 h-auto">
                    <HiOutlineUserRemove />
                  </Button>
                </Tooltip>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <Spacer y={4} />
    </div>
  );
};

export default Subscription;