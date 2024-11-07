import React, { useState, useEffect } from 'react';
import { Container, Table } from 'reactstrap';
import { apiFetch } from '../common/api';

function AllApproverSubscriptionList({ userProfile }) {
  const apiUrl = process.env.REACT_APP_NOTIFICATION_APP_API_URL;
  const [allSubscribers, setAllSubscribers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchSubscribers = async () => {
      try {
        const subscriberData = await apiFetch(`/animal-notify-all-approver-subscriptions`, {
          method: 'POST',
          body: { approver: userProfile.email },
        });
        setAllSubscribers(subscriberData);
      } catch (error) {
        setError(error);
      } finally {
        setLoading(false);
      }
    };

    fetchSubscribers();
  }, [userProfile.email]);

  const handleUnsubscribe = async (token) => {
    try {
      await fetch(`${apiUrl}/external/animal-notify-unsubscribe/${token}`, {
        method: 'GET',
      });
      setAllSubscribers((prevSubscribers) =>
        prevSubscribers.filter((subscriber) => subscriber.token !== token)
      );
    } catch (error) {
      console.error('Error during unsubscription:', error);
    }
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  const subscriberRows = allSubscribers.map((subscriber) => (
    <tr
      key={subscriber.id}
      className={subscriber.email === userProfile.email ? 'highlight-own-subscription' : ''}
    >
      <td>{subscriber.id}</td>
      <td>{subscriber.email}</td>
      <td>{subscriber.approver}</td>
      <td>{subscriber.topic}</td>
      <td>{subscriber.accepted ? 'Yes' : 'No'}</td>
      <td>
        {subscriber.approved && (
          <button onClick={() => handleUnsubscribe(subscriber.token)} className="red lighten-1 waves-effect waves-orange btn-small">
            <i className="small material-icons">person_remove</i>
          </button>
        )}
      </td>
    </tr>
  ));

  return (
    <Container fluid>
      {allSubscribers.length > 0 ? (
        <Table className="highlight responsive-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Email</th>
              <th>Approver</th>
              <th>Topic</th>
              <th>Accepted</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {subscriberRows}
          </tbody>
        </Table>
      ) : (
        <p>No subscribers</p>
      )}
    </Container>
  );
}

export default AllApproverSubscriptionList;
