import React, { useState, useEffect } from 'react';
import { Container, Table } from 'reactstrap';
import { apiFetch } from '../common/api';

function PendingSubscriptionList({ userProfile }) {

    const [pendingSubscribers, setPendingSubscribers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSubscribers = async () => {
            try {
                const pendingData = await apiFetch(`/animal-notify-pending-subscribers`, {
                    method: 'POST',
                    body: { approver: userProfile.email },
                });
                setPendingSubscribers(pendingData);
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchSubscribers();
    }, []);

    const handleApprove = async (email, approver) => {
        try {
            await apiFetch(`/animal-notify-approve-subscriber`, {
                method: 'POST',
                body: {
                    email: email,
                    approver: approver
                },
            });

            setPendingSubscribers((prevPending) =>
                prevPending.filter((subscriber) => subscriber.email !== email)
            );
        } catch (error) {
            console.error('Error approving subscriber:', error);
        }
    };

    const handleReject = async (email) => {
        try {
            await apiFetch(`/animal-notify-reject-subscriber`, {
                method: 'POST',
                body: {
                    email: email
                },
            });

            setPendingSubscribers((prevPending) =>
                prevPending.filter((subscriber) => subscriber.email !== email)
            );
        } catch (error) {
            console.error('Error rejecting subscriber:', error);
        }
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    return (
        <Container fluid>
            {pendingSubscribers.length > 0 ? (
                <div>
                    <p>
                        Please review the list of potential subscribers and approve or reject users as needed.
                    </p>
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
                            {pendingSubscribers.map((subscriber) => (
                                <tr key={subscriber.id}>
                                    <td>{subscriber.id}</td>
                                    <td>{subscriber.email}</td>
                                    <td>{subscriber.approver}</td>
                                    <td>{subscriber.topic}</td>
                                    <td>No</td>
                                    <td>
                                        <button onClick={() => handleApprove(subscriber.email, userProfile.email)} className="waves-effect waves-orange btn-small">
                                            <i className="small material-icons">add_task</i>
                                        </button>
                                        <button onClick={() => handleReject(subscriber.email)} className="red lighten-1 waves-effect waves-orange btn-small">
                                            <i className="small material-icons">unpublished</i>
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </div>
            ) : (
                <p>No pending subscribers</p>
            )}
        </Container>
    );
}

export default PendingSubscriptionList;
