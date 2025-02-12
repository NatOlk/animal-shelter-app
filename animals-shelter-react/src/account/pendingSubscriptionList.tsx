import React, { useEffect, useState, useRef } from 'react';
import {
  Table, TableHeader, TableColumn, TableBody, TableRow, TableCell,
  Tooltip, Button, Spacer, Alert
} from "@nextui-org/react";
import { apiFetch } from '../common/api';
import { TfiReload } from "react-icons/tfi";
import { HiX, HiOutlineUserAdd } from "react-icons/hi";
import { Subscriber, SubscriptionListProps } from "../common/types";

const PendingSubscriptionList: React.FC<SubscriptionListProps> = ({ userProfile }) => {
  const [pendingSubscribers, setPendingSubscribers] = useState<Subscriber[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);
  const hasFetched = useRef(false);

  const fetchSubscribers = async () => {
    setLoading(true);
    setError(null);
    try {
      const pendingData = await apiFetch<Subscriber[]>(`/animal-notify-pending-subscribers`, {
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

  useEffect(() => {
    if (hasFetched.current) return;
    hasFetched.current = true;
    fetchSubscribers();
  }, [userProfile.email]);

  const handleApprove = async (email: string) => {
    try {
      await apiFetch('/animal-notify-approve-subscriber', {
        method: 'POST',
        body: { email, approver: userProfile.email },
      });

      setPendingSubscribers((prev) => prev.filter((subscriber) => subscriber.email !== email));
    } catch (error) {
      setError(error);
    }
  };

  const handleReject = async (email: string) => {
    try {
      await apiFetch('/animal-notify-reject-subscriber', {
        method: 'POST',
        body: { email, approver: userProfile.email },
      });

      setPendingSubscribers((prev) => prev.filter((subscriber) => subscriber.email !== email));
    } catch (error) {
      setError(error);
    }
  };

  return (
    <>
      <Spacer y={5} />
      <Button color="default" variant="faded" onPress={fetchSubscribers}>
        <div className="flex items-center gap-x-2">
          <span>Reload pending subscriptions</span><TfiReload size={20} />
        </div>
      </Button>
      <Spacer y={5} />
      {loading && <div>Loading...</div>}
      {error && (
        <Alert
          dismissable
          color="danger"
          variant="bordered"
          onClose={() => setError(null)}
          title={String(error)}
        />
      )}
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
              <TableRow key={subscriber.id}>
                <TableCell>{subscriber.id}</TableCell>
                <TableCell>{subscriber.email}</TableCell>
                <TableCell>{subscriber.approver || "N/A"}</TableCell>
                <TableCell>{subscriber.topic}</TableCell>
                <TableCell>No</TableCell>
                <TableCell className="w-full md:w-24 flex space-x-2">
                  <Tooltip content="Approve">
                    <Button color="default" variant="light" className="p-2 min-w-2 h-auto"
                      onPress={() => handleApprove(subscriber.email)}>
                      <HiOutlineUserAdd />
                    </Button>
                  </Tooltip>
                  <Tooltip content="Reject">
                    <Button color="default" variant="light" className="p-2 min-w-2 h-auto"
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
        )}
      </Table>
    </>
  );
};

export default PendingSubscriptionList;
