import React, { useEffect, useState, useRef } from 'react';
import {
  Table, TableHeader, TableColumn, TableBody, TableRow, TableCell,
  Tooltip, Button, Spacer, Alert
} from "@nextui-org/react";
import { apiFetch } from '../common/api';
import { TfiReload } from "react-icons/tfi";
import { HiX, HiOutlineUserAdd } from "react-icons/hi";
import { Subscriber, SubscriptionListProps } from "../common/types";

const NoApproverSubscriptionList: React.FC<SubscriptionListProps> = ({ userProfile }) => {
  const [unapprovedSubscribers, setUnapprovedSubscribers] = useState<Subscriber[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);
  const hasFetched = useRef(false);

  useEffect(() => {
    if (hasFetched.current) return;
    hasFetched.current = true;

    const fetchSubscribers = async () => {
      try {
        const unapprovedData = await apiFetch<Subscriber[]>(`/animal-notify-pending-no-approver-subscribers`);
        setUnapprovedSubscribers(unapprovedData);
      } catch (error) {
        setError(error as Error);
      } finally {
        setLoading(false);
      }
    };

    fetchSubscribers();
  }, []);

  const handleApprove = async (email: string) => {
    try {
      await apiFetch('/animal-notify-approve-subscriber', {
        method: 'POST',
        body: { email, approver: userProfile.email },
      });
      setUnapprovedSubscribers((prev) => prev.filter((subscriber) => subscriber.email !== email));
    } catch (error) {
      setError(error);
    }
  };

  const handleReject = async (email: string) => {
    try {
      await apiFetch('/animal-notify-reject-subscriber', {
        method: 'POST',
        body: { email },
      });
      setUnapprovedSubscribers((prev) => prev.filter((subscriber) => subscriber.email !== email));
    } catch (error) {
      setError(error);
    }
  };

  return (
    <>
      <Spacer y={5} />
      <Button color="default" variant="faded" onPress={() => hasFetched.current = false}>
        <div className="flex items-center gap-x-2">
          <span>Reload subscribers without an approver</span><TfiReload size={20} />
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
        {unapprovedSubscribers.length > 0 ? (
          <TableBody>
            {unapprovedSubscribers.map((subscriber) => (
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
                    <Button
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
