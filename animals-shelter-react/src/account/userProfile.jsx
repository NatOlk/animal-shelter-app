import React, { useEffect, useState } from 'react';
import { useQuery } from "@apollo/client";
import AllApproverSubscriptionList from './allApproverSubscriptionList';
import PendingSubscriptionList from './pendingSubscriptionList';
import NoApproverSubscriptionList from './noApproverSubscriptionList';
import UserAnimalTopicSubscriptionStatus from './userAnimalTopicSubscriptionStatus';
import { GET_CURRENT_USER_PROFILE } from '../common/graphqlQueries';
import { apiFetch } from '../common/api';
import { Card, CardHeader, CardBody, CardFooter } from "@nextui-org/card";
import { Divider } from "@nextui-org/divider";
import { Spacer } from "@nextui-org/react";
import { Tabs, Tab } from "@nextui-org/tabs";
import { MdOutlineAlternateEmail, MdGroupAdd } from "react-icons/md";
import { TbUsersGroup } from "react-icons/tb";
import { RiAdminLine } from "react-icons/ri";
import { HiOutlineBellAlert } from "react-icons/hi2";

const UserProfile = () => {
  const { loading, error, data } = useQuery(GET_CURRENT_USER_PROFILE, {
    fetchPolicy: "network-only",
  });

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

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  const { name, email } = data.currentUserProfile;

  return (
    <div className="containerProfile">
      <div className="profileCard">
        <Card className="w-full">
          <CardHeader className="flex gap-3">
            <div className="flex flex-col">
              <h1>User Profile</h1>
            </div>
          </CardHeader>
          <Divider />
          <CardBody>
            <p>Hello, {name}! How are you? Happy to see you!</p>
            <div className="flex items-center space-x-2"><MdOutlineAlternateEmail /> {email}</div>
            <Spacer y={10} />
            <div className="flex w-full flex-col">
              <Tabs aria-label="RolesSubscriptions" size="lg" variant="bordered"
                onSelectionChange={updateSubscriptionStatus}>
                <Tab key="roles" title={
                  <div className="flex items-center space-x-2">
                    <RiAdminLine /><p>Roles</p>
                  </div>}>
                  <div className="profileCardTabContent">
                    <Spacer y={5} />
                    Your current roles: Admin, Employee
                    <Spacer y={20} />
                  </div>
                </Tab>
                <Tab key="subscriptions" title={
                  <div className="flex items-center space-x-2">
                    <HiOutlineBellAlert /><p>Subscriptions</p>
                  </div>}>
                  <div className="profileCardTabContent">
                    <Spacer y={5} />
                    <UserAnimalTopicSubscriptionStatus status={animalNotifyStatusProfile} />
                    <Spacer y={5} />
                  </div>
                </Tab>
              </Tabs>
            </div>
          </CardBody>
          <CardFooter>
          </CardFooter>
        </Card>
      </div>
      <div className="subscriptionsTab">
        <div className="flex flex-col">
          <Tabs aria-label="Subscriptions" size="lg" variant="bordered">
            <Tab key="pending" title={
              <div className="flex items-center space-x-2">
                <MdGroupAdd /><p>Pending subscribers</p>
              </div>}>
              <PendingSubscriptionList userProfile={data.currentUserProfile} />
              <Divider className="my-4" />
              <NoApproverSubscriptionList userProfile={data.currentUserProfile} />
            </Tab>
            <Tab key="all" title={
              <div className="flex items-center space-x-2">
                <TbUsersGroup /><p>All subscribers</p>
              </div>}>
              <AllApproverSubscriptionList userProfile={data.currentUserProfile} />
            </Tab>
          </Tabs>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
