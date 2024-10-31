import React, { useState, useEffect } from 'react';
import { Container, Table } from 'reactstrap';
import { apiFetch } from '../common/api';

function SubscriptionList() {
    const apiUrl = process.env.REACT_APP_NOTIFICATION_APP_API_URL;

    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);
    const [subscribers, setSubscribers] = useState([]);
    const [pendingSubscribers, setPendingSubscribers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSubscribers = async () => {
            try {
                const data = await apiFetch(`/subscribers`);
                const pendingData = await apiFetch(`/pending-subscribers`);

                setSubscribers(data);
                setPendingSubscribers(pendingData);

            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchSubscribers();
    }, []);

    const handleApprove = async (email) => {
        try {
            await apiFetch(`/animal-notify-approve`, {
                method: 'POST',
                body: email,
            });
            setPendingSubscribers((prevPending) =>
                prevPending.filter((subscriber) => subscriber.email !== email)
            );
        } catch (error) {
            console.error('Error approving subscriber:', error);
        }
    };

    const handleUnsubscribe = async (token) => {
        try {
            await fetch(`${apiUrl}/external/animal-notify-unsubscribe/${token}`, {
                method: 'GET',
            });
            setSubscribers((prevSubscribers) =>
                prevSubscribers.filter((subscriber) => subscriber.token !== token)
            );
        } catch (error) {
            console.error('Error during unsubscription:', error);
        }
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    const pageCount = Math.ceil(subscribers.length / perPage);
    const currentSubscribers = subscribers
        .slice(currentPage * perPage, (currentPage + 1) * perPage)
        .map((subscriber) => (
            <tr key={subscriber.id}>
                <td>{subscriber.id}</td>
                <td>{subscriber.email}</td>
                <td>{subscriber.approver}</td>
                <td>{subscriber.topic}</td>
                <td>{subscriber.accepted ? 'Yes' : 'No'}</td>
                <td>
                    <button onClick={() => handleUnsubscribe(subscriber.token)}
                        className="btn btn-primary">
                        Unsubscribe
                    </button>
                </td>
            </tr>
        ));

    const pendingRows = pendingSubscribers.map((subscriber) => (
        <tr key={subscriber.id}>
            <td>{subscriber.id}</td>
            <td>{subscriber.email}</td>
            <td>{subscriber.approver}</td>
            <td>{subscriber.topic}</td>
            <td>No</td>
            <td>
                <button onClick={() => handleApprove(subscriber.email)} className="btn btn-primary">
                    Approve
                </button>
            </td>
        </tr>
    ));

    return (
        <Container fluid>
            <h6>Pending Subscribers</h6>
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
                    {pendingRows}
                </tbody>
            </Table>

            <h6>All Subscribers</h6>
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
                    {currentSubscribers}
                </tbody>
            </Table>

            <div>
                {Array.from({ length: pageCount }, (_, index) => (
                    <button key={index} className="round-button-with-border" onClick={() => setCurrentPage(index)}>
                        {index + 1}
                    </button>
                ))}
            </div>
        </Container>
    );
}

export default SubscriptionList;
