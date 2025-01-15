import React, { useState } from 'react';
import { useAuth } from '../common/authContext';
import { Button, Input } from '@nextui-org/react';
import { LuSmilePlus } from "react-icons/lu";

const QuickSubscribe = () => {
  const { user } = useAuth();
  const [email, setEmail] = useState('');

  const handleSubscribe = async (e) => {
    e.preventDefault();
    try {
      await fetch(`/ansh/notification/external/animal-notify-subscribe`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: email || user.email,
          approver: user.email,
        }),
      });
      setEmail('');
    } catch (error) {
      console.error('Error during subscription:', error);
    }
  };

  return (
    <div className="quick-subscribe-btn">
      <div className="input-field quick-subscribe-input-container">
        <Input
          id="subscribe-email"
          className="min-w-[50px] max-w-[200px]"
          variant="bordered"
          placeholder="Want to subscribe?"
          value={email}
          type="email"
          onChange={(e) => setEmail(e.target.value)}
        />
        <Button
          onPress={handleSubscribe}
          color="default"
          size="sm">
          <LuSmilePlus />
        </Button>
      </div>
    </div>
  );
};

export default QuickSubscribe;
