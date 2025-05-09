import React from 'react';
import Subscription from '../subscription/subscription';
import { Progress } from '@nextui-org/progress';
import { Spacer, Tooltip } from '@nextui-org/react';
import { TbBellRinging } from "react-icons/tb";
import { CgUnavailable } from "react-icons/cg";
import { SubscriptionStatusProps } from "../common/types";

const UserAnimalTopicSubscriptionStatus: React.FC<SubscriptionStatusProps> = ({ status }) => {
  if (status === null) return <p>Status loading...</p>;

  switch (status) {
    case 'NONE':
      return (
        <div>
          <p>You’re currently unsubscribed from our animal updates.</p>
          <p>We highly recommend subscribing to stay informed about all the latest happenings at the shelter!</p>
          <Spacer y={10} />
          <Subscription />
        </div>
      );
    case 'PENDING':
      return (
        <div>
          <p>Your subscription is pending approval. Please wait for an approval email.</p>
          <p>Once you receive it, follow the instructions to complete your subscription activation.</p>
          <Spacer y={10} />
          <Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />
        </div>
      );
    case 'ACTIVE':
      return (
        <div>
          <p>You are subscribed to notifications about animals and their vaccinations, removal of animals, and other updates.</p>
          <p>This will help you stay informed about the activities of our shelter.</p>
          <p>To unsubscribe from these notifications, please review the list of all subscribers, find your email in the list,
            and click the Unsubscribe button.</p>
          <Spacer y={10} />
          <Tooltip data-position="bottom" content="You are subscribed!">
            <TbBellRinging size={32} />
          </Tooltip>
        </div>
      );
    case 'UNKNOWN':
      return (
        <div>
          <p>The notification service is currently unavailable. We are working to restore the connection.</p>
          <p>Your subscription status will be updated as soon as the service becomes available.</p>
          <Spacer y={10} />
           <CgUnavailable size={32}/>
        </div>
      );
    default:
      return <p>Status unknown</p>;
  }
};

export default UserAnimalTopicSubscriptionStatus;
