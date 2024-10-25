import React from 'react';
import Subscription from './subscription';
import { useQuery } from "@apollo/client";
import { GET_CURRENT_USER_PROFILE } from '../common/graphqlQueries.js';

const UserProfile = () => {
console.log("UserProfile component rendered");
  const { loading, error, data } = useQuery(GET_CURRENT_USER_PROFILE);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  const { currentUserProfile } = data;

  return (
    <div className="user-profile">
      {currentUserProfile && (
        <div className="row">
          <ul className="collection with-header">
            <li className="collection-header">User Profile</li>
            <li className="collection-item">Name: {currentUserProfile.name}</li>
            <li className="collection-item">Email: {currentUserProfile.email}</li>
            <li className="collection-header">Roles</li>
            <li className="collection-item">
              <ul className="collection">
                {currentUserProfile.roles && currentUserProfile.roles.length > 0 ? (
                  currentUserProfile.roles.map((role, index) => (
                    <li className="collection-item" key={role}>{role}</li>
                  ))
                ) : (
                  <li>No roles assigned</li>
                )}
              </ul>
            </li>
          </ul>
        </div>
      )}
      <Subscription/>
    </div>
  );
};

export default UserProfile;
