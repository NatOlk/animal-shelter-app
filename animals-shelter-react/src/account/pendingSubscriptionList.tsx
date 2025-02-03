import React, { useState, useEffect } from 'react';
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell, Tooltip, Button } from "@nextui-org/react";
import { apiFetch } from '../common/api';
import { HiX, HiOutlineUserAdd } from "react-icons/hi";
import { Subscriber, SubscriptionListProps } from "../common/types";

const PendingSubscriptionList: React.FC<SubscriptionListProps> = ({ userProfile }) => {
  const [pendingSubscribers, setPendingSubscribers] = useState<Subscriber[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const fetchSubscribers = async () => {
      try {
        const pendingData: Subscriber[] = await apiFetch(`/animal-notify-pending-subscribers`, {
          method: 'POST',
          body: { approver: userProfile.email },
        });
        setPendingSubscribers(pendingData);
      } catch (error) {
        setError(error as Error);
      } finally {
        setLoading(false);
      }
    };

    fetchSubscribers();
  }, [userProfile.email]);

  const handleApprove = async (email: string, approver: string) => {
    try {
      await apiFetch(`/animal-notify-approve-subscriber`, {
        method: 'POST',
        body: { email, approver },
      });

      setPendingSubscribers((prevPending) => prevPending.filter((subscriber) => subscriber.email !== email));
    } catch (error) {
      console.error('Error approving subscriber:', error);
    }
  };

  const handleReject = async (email: string) => {
    try {
      await apiFetch(`/animal-notify-reject-subscriber`, {
        method: 'POST',
        body: { email },
      });

      setPendingSubscribers((prevPending) => prevPending.filter((subscriber) => subscriber.email !== email));
    } catch (error) {
      console.error('Error rejecting subscriber:', error);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <Table>
      <TableHeader>
        <TableColumn>#</TableColumn>
        <TableColumn>Email</TableColumn>
        <TableColumn>Approver</TableColumn>
        <TableColumn>Topic</TableColumn>
        <TableColumn className="w-full md:w-16">Accepted</TableColumn>
        <TableColumn className="w-full md:w-16">Actions</TableColumn>
      </TableHeader>
      {pendingSubscribers.length > 0 ? (
        <TableBody>
          {pendingSubscribers.map((subscriber) => (
            <TableRow
              key={subscriber.id}
              hover
              style={{ backgroundColor: subscriber.email === userProfile.email ? '#f1f1f1' : 'transparent' }}
            >
              <TableCell>{subscriber.id}</TableCell>
              <TableCell>{subscriber.email}</TableCell>
              <TableCell>{subscriber.approver}</TableCell>
              <TableCell>{subscriber.topic}</TableCell>
              <TableCell>No</TableCell>
              <TableCell className="w-full md:w-24">
                <Tooltip content="Approve">
                  <Button
                    color="default"
                    variant="light"
                    className="p-2 min-w-2 h-auto"
                    onPress={() => handleApprove(subscriber.email, userProfile.email)}
                  >
                    <HiOutlineUserAdd />
                  </Button>
                </Tooltip>
                <Tooltip content="Reject">
                  <Button
                    color="default"
                    variant="light"
                    className="p-2 min-w-2 h-auto"
                    onPress={() => handleReject(subscriber.email)}
                  >
                    <HiX />
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
  );
};

export default PendingSubscriptionList;
