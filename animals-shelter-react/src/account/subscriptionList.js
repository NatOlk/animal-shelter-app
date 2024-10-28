import React, { useState, useEffect } from 'react';
import { Container, Table } from 'reactstrap';

function SubscriptionList() {
    const apiUrl = process.env.REACT_APP_NOTIFICATION_APP_API_URL;
    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);
    const [subscribers, setSubscribers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSubscribers = async () => {
            try {
                const response = await fetch(`${apiUrl}/animal-notify-all-subscribes`);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setSubscribers(data);
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchSubscribers();
    }, []);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    const pageCount = Math.ceil(subscribers.length / perPage);
    const currentSubscribers = subscribers
        .slice(currentPage * perPage, (currentPage + 1) * perPage)
        .map(subscriber => (
            <tr key={subscriber.id}>
                <td>{subscriber.id}</td>
                <td>{subscriber.email}</td>
            </tr>
        ));

    return (
        <Container fluid>
            <h6>Subscribers List</h6>
            <Table className="highlight responsive-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Email</th>
                    </tr>
                </thead>
                <tbody>
                    {currentSubscribers}
                </tbody>
            </Table>

            <div>
                {Array.from({ length: pageCount }, (_, index) => (
                    <button key={index} className="round-button-with-border"
                        onClick={() => setCurrentPage(index)}>
                        {index + 1}
                    </button>
                ))}
            </div>
        </Container>
    );
}

export default SubscriptionList;
