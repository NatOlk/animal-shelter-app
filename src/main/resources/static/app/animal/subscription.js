import React, { useState, useEffect } from 'react';

const Subscription = () => {
    const [email, setEmail] = useState('');
    const [subscribers, setSubscribers] = useState([]);

   const loadSubscribers = async () => {
        try {
              const response = await fetch('/subscribers', {
                        cache: 'no-cache', // or 'no-cache', 'reload'
                    });
            if (response.ok) {
                const data = await response.json();
                setSubscribers(data);
            } else {
                console.error('Failed to load subscribers');
                console.error(response);
            }
        } catch (error) {
            console.error('Error fetching subscribers:', error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('/subscribe', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(email),
            });
            if (response.ok) {
                setEmail('');
                loadSubscribers();
            }
        } catch (error) {
            console.error('Error during subscription:', error);
        }
    };

    const handleUnsubscribe = async (email) => {
            try {
                const response = await fetch('/unsubscribe', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify( email ),
                });
                if (response.ok) {
                    loadSubscribers();
                }
            } catch (error) {
                console.error('Error during unsubscription:', error);
            }
        };

   useEffect(() => {
        loadSubscribers();
    }, []);

    return (
    <>
        <form onSubmit={handleSubmit}>
            <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Please insert your email"
                required
            />
            <button type="submit">Subscribe</button>
        </form>
          <div>
                         {subscribers.map((subscriber, index) => (
                             <span key={index}>
                                 {subscriber}
                                 <button onClick={() => handleUnsubscribe(subscriber)}>Unsubscribe</button>
                                 {index !== subscribers.length - 1 ? ', ' : ''}
                             </span>
                         ))}
                     </div>
           </>
    );
};

export default Subscription;
