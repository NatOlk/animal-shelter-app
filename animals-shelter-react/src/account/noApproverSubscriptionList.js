import React, { useState, useEffect } from 'react';
import { Container, Table } from 'reactstrap';
import { apiFetch } from '../common/api';
import M from 'materialize-css';

function NoApproverSubscriptionList({ userProfile }) {
    const [unapprovedSubscribers, setUnapprovedSubscribers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSubscribers = async () => {
            try {
                const unapprovedData = await apiFetch(`/animal-notify-pending-no-approver-subscribers`);
                setUnapprovedSubscribers(unapprovedData);
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchSubscribers();
    }, []);

    useEffect(() => {
        const elemsTooltips = document.querySelectorAll('.tooltipped');
        const instancesTooltips = M.Tooltip.init(elemsTooltips, {});
        return () => {
            instancesTooltips.forEach((instance) => instance.destroy());
        };
    }, [unapprovedSubscribers]);

    const handleApprove = async (email, approver) => {
        try {
            await apiFetch(`/animal-notify-approve-subscriber`, {
                method: 'POST',
                body: {
                    email: email,
                    approver: approver
                },
            });

            setUnapprovedSubscribers((prevSubscribers) =>
                prevSubscribers.filter((subscriber) => subscriber.email !== email)
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

            setUnapprovedSubscribers((prevSubscribers) =>
                prevSubscribers.filter((subscriber) => subscriber.email !== email)
            );
        } catch (error) {
            console.error('Error rejecting subscriber:', error);
        }
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    const subscriberRows = unapprovedSubscribers.map((subscriber) => (
        <tr key={subscriber.id}>
            <td>{subscriber.id}</td>
            <td>{subscriber.email}</td>
            <td>{subscriber.approver}</td>
            <td>{subscriber.topic}</td>
            <td>No</td>
            <td>
                <button
                    onClick={() => handleApprove(subscriber.email, userProfile.email)}
                    className="tooltipped waves-effect waves-orange btn-small"
                    data-position="bottom"
                    data-tooltip="Approve">
                    <i className="small material-icons">add_task</i>
                </button>
                <button
                    onClick={() => handleReject(subscriber.email)}
                    className="tooltipped red lighten-1 waves-effect waves-orange btn-small"
                    data-position="bottom"
                    data-tooltip="Reject">
                    <i className="small material-icons">unpublished</i>
                </button>
            </td>
        </tr>
    ));

    return (
        <Container fluid>
            {unapprovedSubscribers.length > 0 ? (
                <div>
                    <p>These users are attempting to subscribe from an external service.
                        As an admin, you can review their requests and approve them, although this is not your primary responsibility.</p>
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
                </div>
            ) : (
                <p>No subscribers without approver</p>
            )}
        </Container>
    );
}

export default NoApproverSubscriptionList;
