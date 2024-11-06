import React, { useEffect, useState } from 'react';
import Subscription from './subscription';
import AllApproverSubscriptionList from './allApproverSubscriptionList';
import { useQuery } from "@apollo/client";
import PendingSubscriptionList from './pendingSubscriptionList';
import NoApproverSubscriptionList from './noApproverSubscriptionList';
import { GET_CURRENT_USER_PROFILE } from '../common/graphqlQueries';
import { apiFetch } from '../common/api';

const UserProfile = () => {
  const { loading, error, data } = useQuery(GET_CURRENT_USER_PROFILE, {
    fetchPolicy: "network-only"
  });

  const [isNoApproverOpen, setIsNoApproverOpen] = useState(false);
  const [isAllOpen, setIsAllOpen] = useState(false);
  const [animalNotifyStatusProfile, setAnimalNotifyStatusProfile] = useState(null);

  useEffect(() => {
    if (data && data.currentUserProfile) {
      setAnimalNotifyStatusProfile(data.currentUserProfile.animalNotifyStatus || "NONE");
      const elems = document.querySelectorAll('.collapsible');
      const instances = M.Collapsible.init(elems, {});
      const elemsTooltips = document.querySelectorAll('.tooltipped');
      const instancesTooltips = M.Tooltip.init(elemsTooltips, {});

      return () => {
        instances.forEach(instance => instance.destroy());
        instancesTooltips.forEach(instance => instance.destroy());
      };
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
          <span>
            You’re currently unsubscribed from our animal updates. We highly recommend subscribing to stay informed about all the latest happenings at the shelter!
            <Subscription />
          </span>
        );
      case 'PENDING':
        return (
          <span>
            Your subscription is pending approval. Please wait for an approval email. Once you receive it, follow the instructions to complete your subscription activation.
            <div className="progress">
              <div className="indeterminate"></div>
            </div>
          </span>
        );
      case 'ACTIVE':
        return (
          <span>
            <a className="tooltipped" data-position="bottom" data-tooltip="You are subscribed!">
              <i className="medium material-icons green-text text-darken-1">notifications_active</i>
            </a>
          </span>
        );
      default:
        return <p>Status unknown</p>;
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  const { name, email } = data.currentUserProfile;

  return (
    <div>
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
                  <div className="collapsible-header">
                    <i className="material-icons">group</i>Roles
                  </div>
                  <div className="collapsible-body">
                    <span>ADMIN, EMPLOYEE</span>
                  </div>
                </li>
                <li>
                  <div className="collapsible-header" onClick={updateSubscriptionStatus}>
                    <i className="material-icons">place</i>Your subscriptions
                  </div>
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
                  <PendingSubscriptionList userProfile={data.currentUserProfile} />
                </div>
              </li>
              <li>
                <div className="collapsible-header" onClick={() => setIsNoApproverOpen(!isNoApproverOpen)}>
                  <i className="material-icons">no_accounts</i>No Approver Subscribers
                </div>
                <div className="collapsible-body">
                  {isNoApproverOpen && <NoApproverSubscriptionList userProfile={data.currentUserProfile} />}
                </div>
              </li>
              <li>
                <div className="collapsible-header" onClick={() => setIsAllOpen(!isAllOpen)}>
                  <i className="material-icons">how_to_reg</i>All Subscribers
                </div>
                <div className="collapsible-body">
                  {isAllOpen && <AllApproverSubscriptionList userProfile={data.currentUserProfile} />}
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
