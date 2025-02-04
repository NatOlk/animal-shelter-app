import React, { useState } from 'react';
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell,
         Tooltip, Button, Spacer } from "@nextui-org/react";
import { apiFetch } from '../common/api';
import { TfiReload } from "react-icons/tfi";
import { HiX, HiOutlineUserAdd } from "react-icons/hi";
import { Subscriber, SubscriptionListProps } from "../common/types";

const NoApproverSubscriptionList: React.FC<SubscriptionListProps> = ({ userProfile }) => {
  const [unapprovedSubscribers, setUnapprovedSubscribers] = useState<Subscriber[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null);

  const fetchSubscribers = async () => {
    setLoading(true);
    setError(null);
    try {
      const unapprovedData = await apiFetch<Subscriber[]>(`/animal-notify-pending-no-approver-subscribers`);
      setUnapprovedSubscribers(unapprovedData);
    } catch (error) {
      setError(error as Error);
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (email: string) => {
    try {
      await apiFetch('/animal-notify-approve-subscriber', {
        method: 'POST',
        body: { email, approver: userProfile.email },
      });
      setUnapprovedSubscribers((prev) => prev.filter((subscriber) => subscriber.email !== email));
    } catch (err) {
      console.error('Error approving subscriber:', err);
    }
  };

  const handleReject = async (email: string) => {
    try {
      await apiFetch('/animal-notify-reject-subscriber', {
        method: 'POST',
        body: { email },
      });
      setUnapprovedSubscribers((prev) => prev.filter((subscriber) => subscriber.email !== email));
    } catch (err) {
      console.error('Error rejecting subscriber:', err);
    }
  };

  return (
    <>
      <Spacer y={5} />
      <Button color="default" variant="faded" onPress={fetchSubscribers}>
        <div className="flex items-center gap-x-2">
          <span>Load subscribes without assigned approver</span><TfiReload size={20} />
        </div>
      </Button>
      <Spacer y={5} />
      {loading && <div>Loading...</div>}
      {error && <div style={{ color: "red" }}>Error: {error.message}</div>}
      <Table>
        <TableHeader>
          <TableColumn>#</TableColumn>
          <TableColumn>Email</TableColumn>
          <TableColumn>Approver</TableColumn>
          <TableColumn>Topic</TableColumn>
          <TableColumn className="w-full md:w-16">Accepted</TableColumn>
          <TableColumn className="w-full md:w-16">Actions</TableColumn>
        </TableHeader>
        {unapprovedSubscribers.length > 0 ? (
          <TableBody>
            {unapprovedSubscribers.map((subscriber) => (
              <TableRow key={subscriber.id}>
                <TableCell>{subscriber.id}</TableCell>
                <TableCell>{subscriber.email}</TableCell>
                <TableCell>{subscriber.approver || "N/A"}</TableCell>
                <TableCell>{subscriber.topic}</TableCell>
                <TableCell>No</TableCell>
                <TableCell className="w-full md:w-24">
                  <Tooltip content="Approve">
                    <Button
                      color="default" variant="light"
                      className="p-2 min-w-2 h-auto"
                      onPress={() => handleApprove(subscriber.email)}>
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
        )}
      </Table>
    </>
  );
};

export default NoApproverSubscriptionList;
