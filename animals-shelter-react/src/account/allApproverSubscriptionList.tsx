import React, { useState, useEffect } from 'react';
import {
  Table, TableHeader, TableColumn, TableBody, TableRow, TableCell,
  Tooltip, Button, Spacer
} from "@nextui-org/react";
import { apiFetch } from '../common/api';
import { HiOutlineUserRemove } from "react-icons/hi";
import { Subscriber, SubscriptionListProps} from "../common/types";

const AllApproverSubscriptionList: React.FC<SubscriptionListProps> = ({ userProfile }) => {
  const [allSubscribers, setAllSubscribers] = useState<Subscriber[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const fetchSubscribers = async () => {
      try {
        const subscriberData = await apiFetch<Subscriber[]>(`/animal-notify-all-approver-subscriptions`, {
          method: 'POST',
          body: { approver: userProfile.email },
        });
        setAllSubscribers(subscriberData);
      } catch (error) {
        setError(error as Error);
      } finally {
        setLoading(false);
      }
    };
    fetchSubscribers();
  }, [userProfile.email]);

  const handleUnsubscribe = async (token: string) => {
    try {
      await fetch(`/ansh/notification/external/animal-notify-unsubscribe/${token}`, {
        method: 'GET',
      });
      setAllSubscribers((prevSubscribers) =>
        prevSubscribers.filter((subscriber) => subscriber.token !== token)
      );
    } catch (error) {
      console.error('Error during unsubscription:', error);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <>
      <Table>
        <TableHeader>
          <TableColumn>#</TableColumn>
          <TableColumn>Email</TableColumn>
          <TableColumn>Approver</TableColumn>
          <TableColumn>Topic</TableColumn>
          <TableColumn className="w-full md:w-16">Accepted</TableColumn>
          <TableColumn className="w-full md:w-16">Actions</TableColumn>
        </TableHeader>
        {allSubscribers.length > 0 ? (
          <TableBody>
            {allSubscribers.map((subscriber) => (
              <TableRow
                key={subscriber.id}
                style={{
                  backgroundColor:
                    subscriber.email === userProfile.email ? '#f0f8ff' : 'inherit',
                }}
              >
                <TableCell>{subscriber.id}</TableCell>
                <TableCell>{subscriber.email}</TableCell>
                <TableCell>{subscriber.approver}</TableCell>
                <TableCell>{subscriber.topic}</TableCell>
                <TableCell>{subscriber.accepted ? 'Yes' : 'No'}</TableCell>
                <TableCell className="w-full md:w-24">
                  {subscriber.approved && (
                    <Tooltip content="Unsubscribe" placement="bottom">
                      <Button
                        color="default" variant="light"
                        className="p-2 min-w-2 h-auto"
                        onPress={() => handleUnsubscribe(subscriber.token)}>
                        <HiOutlineUserRemove />
                      </Button>
                    </Tooltip>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        ) : (
          <TableBody emptyContent={"No rows to display."}>{[]}</TableBody>
        )}
      </Table>
      <Spacer y={10} />
    </>
  );
};

export default AllApproverSubscriptionList;
