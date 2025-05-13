import React, { useState } from 'react';
import { useAuth } from '../common/authContext';
import { Button, Input } from '@nextui-org/react';
import { LuSmilePlus } from "react-icons/lu";

const QuickSubscribe: React.FC = () => {
  const { user } = useAuth();
  const [email, setEmail] = useState<string>('');

  const handleSubscribe = async () => {
    await fetch(`/ansh/notification/external/animal-notify-subscribe`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: email || user?.email,
        approver: user?.email,
      }),
    });
    setEmail('');
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
        <Button onPress={handleSubscribe} color="default" size="sm">
          <LuSmilePlus />
        </Button>
      </div>
    </div>
  );
};

export default QuickSubscribe;
