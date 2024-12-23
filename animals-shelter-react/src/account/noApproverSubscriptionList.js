import React, { useState } from 'react';
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@nextui-org/react";
import { apiFetch } from '../common/api';
import { Tooltip, Button, Spacer, Link } from "@nextui-org/react";
import { TfiReload } from "react-icons/tfi";
import { HiX } from "react-icons/hi";
import { HiOutlineUserAdd } from "react-icons/hi";

export default function NoApproverSubscriptionList({ userProfile }) {
  const [unapprovedSubscribers, setUnapprovedSubscribers] = useState([]); // Данные таблицы
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchSubscribers = async () => {
    setLoading(true);
    setError(null);
    try {
      const unapprovedData = await apiFetch(`/animal-notify-pending-no-approver-subscribers`);
      setUnapprovedSubscribers(unapprovedData);
    } catch (error) {
      setError(error);
    } finally {
      setLoading(false);
    }
  };

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

  return (
    <>
      <Spacer y={5} />
      <Button color="default"
        variant="faded"
        onPress={fetchSubscribers}>
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
        )}
      </Table>
    </>
  );
}
