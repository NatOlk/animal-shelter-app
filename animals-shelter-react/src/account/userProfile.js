"use client"
import React, { useEffect, useState } from 'react';
import Subscription from './subscription';
import AllApproverSubscriptionList from './allApproverSubscriptionList';
import { useQuery } from "@apollo/client";
import PendingSubscriptionList from './pendingSubscriptionList';
import NoApproverSubscriptionList from './noApproverSubscriptionList';
import { GET_CURRENT_USER_PROFILE } from '../common/graphqlQueries';
import { apiFetch } from '../common/api';
import { Card, CardHeader, CardBody, CardFooter } from "@nextui-org/card";
import { Divider } from "@nextui-org/divider";
import { Progress } from "@nextui-org/progress";
import { Tooltip, Button, Spacer } from "@nextui-org/react";
import { Tabs, Tab } from "@nextui-org/tabs";

const UserProfile = () => {
  const { loading, error, data } = useQuery(GET_CURRENT_USER_PROFILE, {
    fetchPolicy: "network-only"
  });
  const [isNoApproverOpen, setIsNoApproverOpen] = useState(false);
  const [isAllOpen, setIsAllOpen] = useState(false);
  const [animalNotifyStatusProfile, setAnimalNotifyStatusProfile] = useState(null);

  useEffect(() => {
    if (data?.currentUserProfile) {
      setAnimalNotifyStatusProfile(data.currentUserProfile.animalNotifyStatus || "NONE");
    }
  }, [data]);

  const updateSubscriptionStatus = async () => {
    try {
      const status = await apiFetch(`/animal-notify-approver-status`, {
        method: 'POST',
        body: { approver: data.currentUserProfile.email },
      });

      setAnimalNotifyStatusProfile(status || "NONE");
    } catch (err) {
      console.error("Error updating subscription status:", err);
    }
  };

  const renderIconBasedOnStatus = () => {
    if (animalNotifyStatusProfile === null) return <p>Status loading...</p>;

    switch (animalNotifyStatusProfile) {
      case 'NONE':
        return (
          <div>
            <p className="text-small text-default-500">
              You’re currently unsubscribed from our animal updates.
              We highly recommend subscribing to stay informed about all the latest happenings at the shelter!
            </p>
            <Spacer y={10} />
            <Subscription />
          </div>
        );
      case 'PENDING':
        return (
          <div>
            <p className="text-small text-default-500">
              Your subscription is pending approval. Please wait for an approval email.
              Once you receive it, follow the instructions to complete your subscription activation.
            </p>
            <Spacer y={10} />
            <Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />
          </div >
        );
      case 'ACTIVE':
        return (
          <div>
            <p className="text-small text-default-500">
              You are subscribed to notifications about animals and their vaccinations, removal of animals, and other updates.
              This will help you stay informed about the activities of our shelter.
              To unsubscribe from these notifications, please review the list of all subscribers, find your email in the list,
              and click the Unsubscribe button.
            </p>
            <Spacer y={10} />
            <Tooltip data-position="bottom" content="You are subscribed!">
              <i className="small material-icons green-text text-darken-1">notifications_active</i>
            </Tooltip>
          </div>
        );
      default:
        return <p>Status unknown</p>;
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  const { name, email } = data.currentUserProfile;

  return (
    <>
      <Card className="w-full">
        <CardHeader className="flex gap-3">
          <div className="flex flex-col">
            <p className="text-md">User profile</p>
          </div>
        </CardHeader>
        <Divider />
        <CardBody>
          <p className="text-small text-default-500">Hello, {name}! How are you? Happy to see you!</p>
          <p className="text-small text-default-500"><i className="small material-icons">alternate_email</i> {email}</p>
          <Spacer y={10} />
          <div className="flex w-full flex-col">
            <Tabs aria-label="Options" size="lg" variant="light" onSelectionChange={updateSubscriptionStatus}>
              <Tab key="roles" title="Roles">
                <Card className="max-w-[400px]">
                  <CardBody>
                    Admin, Employee
                    <Spacer y={20} />
                  </CardBody>
                </Card>
              </Tab>
              <Tab key="subscriptions" title="Subscriptions">
                <Card className="max-w-[800px]">
                  <CardBody>
                    {renderIconBasedOnStatus()}
                    <Spacer y={20} />
                  </CardBody>
                </Card>
              </Tab>
            </Tabs>
          </div>
        </CardBody>
        <CardFooter>
        </CardFooter>
      </Card>
      <Spacer y={20} />
      <div className="flex w-full flex-col">
        <Tabs aria-label="Options" size="lg" variant="bordered">
          <Tab key="pending" title="Pending subscribers">
            <Card>
              <CardBody>
                <PendingSubscriptionList userProfile={data.currentUserProfile} />
                <Divider className="my-4" />
                <NoApproverSubscriptionList userProfile={data.currentUserProfile} />
              </CardBody>
            </Card>
          </Tab>
          <Tab key="all" title="All subscribers">
            <Card>
              <CardBody>
                <AllApproverSubscriptionList userProfile={data.currentUserProfile} />
              </CardBody>
            </Card>
          </Tab>
        </Tabs>
      </div>
    </>
  );
};

export default UserProfile;
