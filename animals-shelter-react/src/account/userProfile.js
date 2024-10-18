import React from 'react';
import Subscription from './subscription';
import { useQuery } from "@apollo/client";
import { GET_CURRENT_USER } from '../common/graphqlQueries.js';

const UserProfile = () => {
  const { loading, error, data } = useQuery(GET_CURRENT_USER);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  const { currentUser } = data;

  return (
    <div className="user-profile">
      {currentUser && (
        <div class="row">
          <ul class="collection with-header">
            <li class="collection-header">User Profile</li>
            <li class="collection-item">Name: {currentUser.name}</li>
            <li class="collection-item">Email: {currentUser.email}</li>
            <li class="collection-item"><Subscription email={currentUser.email} /></li>
            <li class="collection-header">Roles</li>
            <li class="collection-item">
              <ul class="collection">
                {currentUser.roles && currentUser.roles.length > 0 ? (
                  currentUser.roles.map((role, index) => (
                    <li class="collection-item" key={role}>{role}</li>
                  ))
                ) : (
                  <li>No roles assigned</li>
                )}
              </ul>
            </li>
          </ul>
        </div>
      )}
    </div>
  );
};

export default UserProfile;
