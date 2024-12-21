import React, { useState, useEffect } from 'react';
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@nextui-org/react";
import { apiFetch } from '../common/api';
import { Tooltip, Button, Spacer } from "@nextui-org/react";

export default function NoApproverSubscriptionList({ userProfile }) {
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

  const handleApprove = async (email) => {
    try {
      await apiFetch('/animal-notify-approve-subscriber', {
        method: 'POST',
        body: JSON.stringify({ email, approver: userProfile.email }),
      });
      setUnapprovedSubscribers((prev) =>
        prev.filter((subscriber) => subscriber.email !== email)
      );
    } catch (err) {
      console.error('Error approving subscriber:', err);
    }
  };

  const handleReject = async (email) => {
    try {
      await apiFetch('/animal-notify-reject-subscriber', {
        method: 'POST',
        body: JSON.stringify({ email }),
      });
      setUnapprovedSubscribers((prev) =>
        prev.filter((subscriber) => subscriber.email !== email)
      );
    } catch (err) {
      console.error('Error rejecting subscriber:', err);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <>
      <Spacer y={10} />
      <h1>Subscribers Without Assigned Approver</h1>
      <Spacer y={10} />
      <Table>
        <TableHeader>
          <TableColumn>#</TableColumn>
          <TableColumn>Email</TableColumn>
          <TableColumn>Approver</TableColumn>
          <TableColumn>Topic</TableColumn>
          <TableColumn>Accepted</TableColumn>
          <TableColumn>Actions</TableColumn>
        </TableHeader>
        {unapprovedSubscribers.length > 0 ? (

          <TableBody>
            {unapprovedSubscribers.map((subscriber) => (
              <TableRow key={subscriber.id}>
                <TableCell>{subscriber.id}</TableCell>
                <TableCell>{subscriber.email}</TableCell>
                <TableCell>{subscriber.approver}</TableCell>
                <TableCell>{subscriber.topic}</TableCell>
                <TableCell>No</TableCell>
                <TableCell className="w-full md:w-24">
                  <Tooltip content="Approve">
                    <Button
                      color="default" variant="light"
                      className="p-2 min-w-2 h-auto"
                      onPress={() => handleApprove(subscriber.email)}>
                      Approve
                    </Button>
                  </Tooltip>
                  <Tooltip content="Reject">
                    <Button
                      color="default" variant="light"
                      className="p-2 min-w-2 h-auto"
                      onPress={() => handleReject(subscriber.email)}>
                      Reject
                    </Button>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>

        ) : (
          <TableBody emptyContent={"No rows to display."}>{[]}</TableBody>
        )}
      </Table>
    </>
  );
}
