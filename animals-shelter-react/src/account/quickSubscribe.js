import React, { useState } from 'react';
import { useAuth } from '../common/authContext';

const QuickSubscribe = () => {
  const { user } = useAuth();
  const [email, setEmail] = useState('');
  const apiUrl = process.env.REACT_APP_NOTIFICATION_APP_API_URL;

  const handleSubscribe = async (e) => {
    e.preventDefault();
    try {
      await fetch(`${apiUrl}/external/animal-notify-subscribe`, {
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
    <div className="fixed-action-btn" style={{ bottom: '45px', right: '24px' }}>
      <div
        className="input-field"
        style={{  display: 'flex', alignItems: 'center', }}>
        <input
          id="subscribe-email"
          type="email"
          placeholder="Want to subscribe?"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          style={{ marginRight: '8px', }} />
        <button onClick={handleSubscribe} className="waves-effect waves-orange btn-small" >
          <i className="small material-icons">add_reaction</i>
        </button>
      </div>
    </div>
  );
};

export default QuickSubscribe;
