import React from 'react';
import { useAuth } from '../common/authContext';
import { Button, Spacer } from "@nextui-org/react";
import { LuSmilePlus } from "react-icons/lu";
import { topics, TopicKey } from '../common/types';
import { HiOutlineUserRemove } from "react-icons/hi";

const Subscription: React.FC = () => {
  const { user, isAdmin } = useAuth();

  const handleSubscribe = async (topicKey: TopicKey) => {
    if (!user?.email) return;

    try {
      const body: Record<string, any> = {
        email: user.email,
        topic: topicKey,
      };

      if (isAdmin) {
        body.approver = user.email;
      }

      await fetch(`/ansh/notification/external/subscriptions/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });

      // TODO: add status update later
    } catch (error) {
      // TODO: handle error later
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
              <td className="py-3 px-4">
                <div className="font-medium">{label}</div>
              </td>
              <td className="py-3 px-4">
                <div className="font-small">{description}</div>
              </td>
              <td className="py-3 px-4">{/* Placeholder for future status */}</td>
              <td className="py-3 px-4 flex gap-2">
                <Button
                  onPress={() => handleSubscribe(key)}
                  color="default"
                  size="sm"
                  startContent={<LuSmilePlus />}/>
                <Button
                  isDisabled
                  color="danger"
                  size="sm"
                  startContent={<HiOutlineUserRemove />}/>
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