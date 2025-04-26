import React, { useEffect, useState } from "react";
import {
  Table, TableHeader, TableColumn, TableBody, TableRow, TableCell,
  Select, SelectItem, Spacer, Alert
} from "@nextui-org/react";
import { useQuery, useMutation } from "@apollo/client";
import { GET_NON_ADMIN_USERS, UPDATE_USER_ROLES } from "../common/graphqlQueries";
import { UserProfile } from "../common/types";

const ALL_ROLES = ["USER", "EMPLOYEE", "VOLUNTEER", "DOCTOR", "ADMIN"];

const UsersList: React.FC = () => {
  const { data, loading } = useQuery<{ allNonAdminUsers: UserProfile[] }>(GET_NON_ADMIN_USERS, {
    fetchPolicy: "network-only",
  });
  const [updateRoles] = useMutation(UPDATE_USER_ROLES);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleRoleChange = async (userId: string, newRoles: Set<string>) => {
    try {
      updateRoles({
        variables: {
          username: userId,
          roles: Array.from(newRoles),
        },
      });
    } catch (err: any) {
      setErrorMessage(err.message || "Failed to update roles");
    }
  };

  return (
    <>
      {loading && <div>Loading...</div>}
      {errorMessage && (
        <Alert
          color="danger"
          variant="bordered"
          onClose={() => setErrorMessage(null)}
          title={errorMessage}/>
      )}
      {!loading && data?.allNonAdminUsers && (
        <>
        <h4>Please change roles here by selecting roles in the list</h4>
        <Spacer y={10} />
        <Table className="table-fixed w-full">
          <TableHeader>
            <TableColumn>Name</TableColumn>
            <TableColumn>Email</TableColumn>
            <TableColumn>Roles</TableColumn>
          </TableHeader>
          <TableBody>
            {data.allNonAdminUsers.map((user: UserProfile) => (
              <TableRow key={user.id}>
                <TableCell className="w-full w-48">{user.name}</TableCell>
                <TableCell className="w-full w-48">{user.email}</TableCell>
                <TableCell>
                  <Select
                    selectionMode="multiple"
                    selectedKeys={new Set(user.roles)}
                    onSelectionChange={(keys) => handleRoleChange(user.name, keys as Set<string>)}
                    className="max-w-md">
                    {ALL_ROLES.map((role) => (
                      <SelectItem key={role} value={role}>
                        {role.charAt(0) + role.slice(1).toLowerCase()}
                      </SelectItem>
                    ))}
                  </Select>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        </>
      )}
      <Spacer y={10} />
    </>
  );
};

export default UsersList;
