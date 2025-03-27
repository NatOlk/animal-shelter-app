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
import { Tabs, Tab } from "@nextui-org/tabs";
import { MdOutlineAlternateEmail, MdGroupAdd } from "react-icons/md";
import { TbUsersGroup } from "react-icons/tb";
import { RiAdminLine } from "react-icons/ri";
import { HiOutlineBellAlert } from "react-icons/hi2";
import { Spacer, Select, SelectItem } from "@nextui-org/react";
import { UserProfileData } from "../common/types";
import { useAuth } from "../common/authContext";

const ALL_ROLES = ["USER", "EMPLOYEE", "VOLUNTEER", "DOCTOR", "ADMIN"];

const UserProfile: React.FC = () => {
  const { loading, error, data } = useQuery<UserProfileData>(GET_CURRENT_USER_PROFILE, {
    fetchPolicy: "network-only",
  });

  const { user } = useAuth();
  const isAdmin = user?.roles?.includes("ADMIN");
  console.log(isAdmin);
  const [animalNotifyStatusProfile, setAnimalNotifyStatusProfile] = useState<'NONE' | 'PENDING' | 'ACTIVE' | null>(null);
  const [selectedRoles, setSelectedRoles] = useState<Set<string>>(new Set());

  useEffect(() => {
    if (data?.currentUserProfile) {
      setAnimalNotifyStatusProfile(data.currentUserProfile.animalNotifyStatus || "NONE");

      const roles = data.currentUserProfile.roles || [];
      setSelectedRoles(new Set(roles));
    }
  }, [data]);

  const updateSubscriptionStatus = async () => {
    if (!data?.currentUserProfile.email) return;
    const status = await apiFetch<'NONE' | 'PENDING' | 'ACTIVE'>(`/api/animal-notify-approver-status`, {
      method: 'POST',
      body: { approver: data.currentUserProfile.email },
    });
    setAnimalNotifyStatusProfile(status || "NONE");
  };

  const handleRoleChange = (keys: Set<string>) => {
    setSelectedRoles(keys);
    //TODO add roles changes
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
              <h1>User</h1>
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
                    <Select
                      label="Your current roles"
                      selectionMode="multiple"
                      selectedKeys={selectedRoles}
                      onSelectionChange={handleRoleChange}
                      isDisabled={!isAdmin}
                      className="max-w-md">
                      {ALL_ROLES.map((role) => (
                        <SelectItem key={role} value={role}>
                          {role.charAt(0) + role.slice(1).toLowerCase()}
                        </SelectItem>
                      ))}
                    </Select>
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
          <CardFooter />
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
