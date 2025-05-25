import React, { useEffect, useState } from 'react';
import AllApproverSubscriptionList from '../subscription/allApproverSubscriptionList';
import PendingSubscriptionList from '../subscription/pendingSubscriptionList';
import NoApproverSubscriptionList from '../subscription/noApproverSubscriptionList';
import Subscription from '../subscription/subscription';
import { Card, CardHeader, CardBody, CardFooter } from "@nextui-org/card";
import { Divider } from "@nextui-org/divider";
import { Tabs, Tab } from "@nextui-org/tabs";
import { MdOutlineAlternateEmail, MdGroupAdd } from "react-icons/md";
import { TbUsersGroup } from "react-icons/tb";
import { RiAdminLine } from "react-icons/ri";
import { HiOutlineBellAlert } from "react-icons/hi2";
import { Spacer, Select, SelectItem } from "@nextui-org/react";
import { useAuth } from "../common/authContext";
import { UPDATE_USER_ROLES } from "../common/graphqlQueries";
import { useMutation } from "@apollo/client";

const ALL_ROLES = ["USER", "EMPLOYEE", "VOLUNTEER", "DOCTOR", "ADMIN"];

const UserProfile: React.FC = () => {
  const { user, isAdmin, setUser } = useAuth();
  const [selectedRoles, setSelectedRoles] = useState<Set<string>>(new Set(user?.roles || []));
  const [updateUserRoles] = useMutation(UPDATE_USER_ROLES);

  useEffect(() => {
    setSelectedRoles(new Set(user?.roles || []));
  }, [user]);

  const handleRoleChange = (keys: Set<string>) => {
    if (isAdmin && !keys.has("ADMIN")) {
      alert("You cannot remove the ADMIN role from yourself.");
      return;
    }

    setSelectedRoles(keys);

    if (isAdmin && user?.name) {
      updateUserRoles({
        variables: {
          username: user.name,
          roles: Array.from(keys),
        },
      }).then((result) => {
        if (result.data) {
          setUser({
            ...user,
            roles: Array.from(keys),
          });
        }
      }).catch((error: any) => {
        console.error(err.message || "Failed to update user roles:");
      });
    }
  };

  return (
    <div className="containerProfile">
      <div className="profileCard">
        <Card className="w-full">
          <CardHeader className="flex gap-3 card-header-main">
            <div className="flex flex-col">
              <h4 className="text-medium font-medium">User</h4>
            </div>
          </CardHeader>
          <Divider />
          <CardBody>
            <p>Hello, {user?.name}! How are you? Happy to see you!</p>
            <div className="flex items-center space-x-2">
              <MdOutlineAlternateEmail /> {user?.email}</div>
            <Spacer y={10} />
            <div className="flex w-full flex-col">
              <Tabs aria-label="RolesSubscriptions" size="lg" variant="bordered">
                <Tab key="roles" title={
                  <div className="flex items-center space-x-2">
                    <RiAdminLine /><p>My Roles</p>
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
                    <HiOutlineBellAlert /><p>My Subscriptions</p>
                  </div>}>
                  <div className="profileCardTabContent">
                    <Spacer y={5} />
                    <Subscription />
                    <Spacer y={5} />
                  </div>
                </Tab>
              </Tabs>
            </div>
          </CardBody>
          <CardFooter />
        </Card>
      </div>

      {isAdmin && (
        <div className="subscriptionsTab">
          <div className="flex flex-col">
            <Tabs aria-label="Subscriptions" size="lg" variant="bordered">
              <Tab key="pending" title={
                <div className="flex items-center space-x-2">
                  <MdGroupAdd /><p>Pending subscribers</p>
                </div>}>
                <PendingSubscriptionList userProfile={user} />
                <Divider className="my-4" />
                <NoApproverSubscriptionList userProfile={user} />
              </Tab>
              <Tab key="all" title={
                <div className="flex items-center space-x-2">
                  <TbUsersGroup /><p>All subscribers</p>
                </div>}>
                <AllApproverSubscriptionList userProfile={user} />
              </Tab>
            </Tabs>
          </div>
        </div>)}
    </div>
  );
};

export default UserProfile;
