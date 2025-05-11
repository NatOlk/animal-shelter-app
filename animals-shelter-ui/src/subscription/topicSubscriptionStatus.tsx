import React from 'react';
import { Progress } from '@nextui-org/progress';
import { Tooltip } from '@nextui-org/react';
import { TbBellRinging } from "react-icons/tb";
import { CgUnavailable } from "react-icons/cg";
import { MdOutlineMailLock } from "react-icons/md";
import { SubscriptionStatusProps } from "../common/types";

const TopicSubscriptionStatus: React.FC<SubscriptionStatusProps> = ({ status }) => {
  if (status === null) return <p>Status loading...</p>;

  switch (status) {
    case 'NONE':
      return (
        <div>
          <Tooltip data-position="bottom" content="You’re currently unsubscribed from our animal updates.">
            <MdOutlineMailLock size={24} />
          </Tooltip>
        </div>
      );
    case 'PENDING':
      return (
        <div>
          <Tooltip data-position="bottom" content="Your subscription is pending approval.">
            <Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />
          </Tooltip>
        </div>
      );
    case 'ACTIVE':
      return (
        <div>
          <Tooltip data-position="bottom" content="You are subscribed!">
            <TbBellRinging size={32} />
          </Tooltip>
        </div>
      );
    case 'UNKNOWN':
      return (
        <div>
          <Tooltip data-position="bottom" content="The notification service is currently unavailable. We are working to restore the connection.">
            <CgUnavailable size={32} />
          </Tooltip>
        </div>
      );
    default:
      return <p>Status unknown</p>;
  }
};

export default TopicSubscriptionStatus;
