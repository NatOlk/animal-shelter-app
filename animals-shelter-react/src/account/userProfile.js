import React from 'react';
import { useQuery, gql } from '@apollo/client';
import Subscription from './subscription';

const GET_CURRENT_USER = gql`
  query {
    currentUser {
      id
      name
      email
      roles
    }
  }
`;

const UserProfile = () => {
  const { loading, error, data } = useQuery(GET_CURRENT_USER);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  const { currentUser } = data;

  return (
    <div className="user-profile">
      <h4>User Profile</h4>
      {currentUser && (
        <div>
          <h2>User Details</h2>
          <p><strong>Name:</strong> {currentUser.name}</p>
          <p><strong>Email:</strong> {currentUser.email}</p>
          <p><Subscription email={currentUser.email} /></p>
          <h3>Roles</h3>
          <ul>
            {currentUser.roles && currentUser.roles.length > 0 ? (
              currentUser.roles.map((role, index) => (
                <li key={index}>{role}</li>
              ))
            ) : (
              <li>No roles assigned</li>
            )}
          </ul>
        </div>
      )}
    </div>
  );
};

export default UserProfile;
