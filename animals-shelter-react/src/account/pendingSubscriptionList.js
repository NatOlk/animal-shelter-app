import React, { useState, useEffect } from 'react';
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@nextui-org/react";
import { apiFetch } from '../common/api';
import { Tooltip, Button, Spacer } from "@nextui-org/react";
import { HiX } from "react-icons/hi";
import { HiOutlineUserAdd } from "react-icons/hi";

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

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <>
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
        {pendingSubscribers.length > 0 ? (
          <TableBody>
            {pendingSubscribers.map((subscriber) => (
              <TableRow
                key={subscriber.id}
                hover
                style={{
                  backgroundColor: subscriber.email === userProfile.email ? '#f1f1f1' : 'transparent',
                }}>
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
                      onPress={() => handleApprove(subscriber.email, userProfile.email)}>
                      <HiOutlineUserAdd />
                    </Button>
                  </Tooltip>
                  <Tooltip content="Reject">
                    <Button
                      color="default" variant="light"
                      className="p-2 min-w-2 h-auto"
                      onPress={() => handleReject(subscriber.email)}>
                      <HiX />
                    </Button>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        ) : (
          <TableBody emptyContent={"No rows to display."}>{[]}</TableBody>
        )
        }
      </Table>
    </>
  );
}

export default PendingSubscriptionList;
