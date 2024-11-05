import React, { useEffect, useState } from 'react';
import Subscription from './subscription';
import AllApproverSubscriptionList from './allApproverSubscriptionList';
import { useQuery } from "@apollo/client";
import PendingSubscriptionList from './pendingSubscriptionList';
import NoApproverSubscriptionList from './noApproverSubscriptionList';
import { GET_CURRENT_USER_PROFILE } from '../common/graphqlQueries';

const UserProfile = () => {
  const { loading, error, data } = useQuery(GET_CURRENT_USER_PROFILE, {
    fetchPolicy: "network-only"
  });

  const [isNoApproverOpen, setIsNoApproverOpen] = useState(false);
  const [isAllOpen, setIsAllOpen] = useState(false);

  useEffect(() => {
    if (data) {
      const elems = document.querySelectorAll('.collapsible');
      const options = {};
      const instances = M.Collapsible.init(elems, options);

      return () => {
        instances.forEach(instance => instance.destroy());
      };
    }
  }, [data]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  const { currentUserProfile } = data;
  const { name, email, animalNotifyStatus } = currentUserProfile;

  const renderIconBasedOnStatus = () => {
    switch (animalNotifyStatus) {
      case 'NONE':
        return (
          <span>
            <i className="small material-icons">unsubscribe</i> You’re currently unsubscribed from our animal updates.
            We highly recommend subscribing to stay informed about all the latest happenings at the shelter!
            <Subscription />
          </span>
        );
      case 'PENDING':
        return (
          <span>
            <div className="progress">
              <div className="indeterminate"></div>
            </div>
          </span>
        );
      case 'ACTIVE':
        return (
          <span>
            <i className="medium material-icons green-text text-darken-1">
              notifications_active
            </i> You are subscribed to the animal notifications.
          </span>
        );
      default:
        return <p>Status loading...</p>;
    }
  };

  const toggleNoApproverList = () => {
    setIsNoApproverOpen(!isNoApproverOpen);
  };

  const toggleAllList = () => {
    setIsAllOpen(!isAllOpen);
  };

  return (
    <div>
      {currentUserProfile && (
        <div className="row">
          <div className="col s12 m12">
            <div className="card card-color">
              <div className="card-content">
                <span className="card-title">User profile</span>
                <p>Hello, {name}! How are you? Happy to see you!</p>
                <p><i className="small material-icons">alternate_email</i> {email}</p>
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

          <div className="row">
            <div className="col s12 m12">
              <ul className="collapsible card-color">
                <li className="active">
                  <div className="collapsible-header">
                    <i className="material-icons">pending_actions</i>Pending Subscribers</div>
                  <div className="collapsible-body">
                    <PendingSubscriptionList userProfile={currentUserProfile} />
                  </div>
                </li>
                <li>
                  <div className="collapsible-header" onClick={toggleNoApproverList}>
                    <i className="material-icons">no_accounts</i>No Approver Subscribers
                  </div>
                  <div className="collapsible-body">
                    {isNoApproverOpen && <NoApproverSubscriptionList userProfile={currentUserProfile} />}
                  </div>
                </li>
                <li>
                  <div className="collapsible-header" onClick={toggleAllList}>
                    <i className="material-icons">how_to_reg</i>All Subscribers
                  </div>
                  <div className="collapsible-body">
                    {isAllOpen && <AllApproverSubscriptionList userProfile={currentUserProfile} />}
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserProfile;
