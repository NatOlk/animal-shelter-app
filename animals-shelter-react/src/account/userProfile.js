import React, { useEffect } from 'react';
import Subscription from './subscription';
import SubscriptionList from './subscriptionList';
import { useQuery } from "@apollo/client";
import { GET_CURRENT_USER_PROFILE } from '../common/graphqlQueries.js';

const UserProfile = () => {
  const { loading, error, data } = useQuery(GET_CURRENT_USER_PROFILE);

  useEffect(() => {
    if (data) {
      const elems = document.querySelectorAll('.collapsible');
      const options = {};
      M.Collapsible.init(elems, options);

    }
  }, [data]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  const { currentUserProfile } = data;

  const renderIconBasedOnStatus = () => {
    switch (currentUserProfile.animalNotifyStatus) {
      case 'NONE':
        return <span>
          <i className="small material-icons">unsubscribe</i> You’re currently unsubscribed from our animal updates.
          We highly recommend subscribing to stay informed about all the latest happenings at the shelter!
          <Subscription email={currentUserProfile.email} approver={currentUserProfile.email} readOnly={false} />
        </span>;
      case 'PENDING':
        return (<span>
          <i className="small material-icons amber-text text-accent-4">
            hourglass_empty
          </i>
          Your subscription status is in progress. Once it is approved, you will start receiving notifications.
          <Subscription email={currentUserProfile.email} approver={currentUserProfile.email} readOnly={true} />
        </span>
        );
      case 'ACTIVE':
        return (
          <span>
            <i className="small material-icons green-text text-darken-1">
              notifications_active
            </i> You are subscribed to the animal notifications.
            <Subscription email={currentUserProfile.email} approver={currentUserProfile.email} readOnly={true} />
          </span>
        );
      default:
        return <p>Status loading...</p>;
    }
  };

  return (
    <div>
      {currentUserProfile && (
        <div className="row">
          <div className="col s12 m12">
            <div className="card card-color">
              <div className="card-content">
                <span className="card-title">User profile</span>
                <p>Hello, {currentUserProfile.name}! How are you? Happy to see you!</p>
                <p><i className="small material-icons">alternate_email</i> {currentUserProfile.email}</p>
              </div>
              <div className="card-action">
                <ul className="collapsible">
                  <li>
                    <div className="collapsible-header"><i className="material-icons">group</i>Roles</div>
                    <div className="collapsible-body"><span>ADMIN, EMPLOYEE</span></div>
                  </li>
                  <li>
                    <div className="collapsible-header"><i className="material-icons">place</i>Your subscriptions</div>
                    <div className="collapsible-body">
                      {renderIconBasedOnStatus()}
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <SubscriptionList />
        </div>
      )}
    </div>
  );
};

export default UserProfile;
