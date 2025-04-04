import React from "react";
import { useAuth } from './common/authContext';
import { Divider } from "@nextui-org/divider";
import { Link, useLocation } from "react-router-dom";
import Logout from './logout';

const SideBarContent: React.FC = () => {
  const location = useLocation();
  const isProfile = location.pathname === "/profile" || location.pathname === "/usersList";

  return (
    <div className="max-w-md">
      {isProfile ? (
        <>
          <div className="space-y-1">
            <Link to="/" color="foreground">
              <h4 className="text-medium font-medium">Back to Animals</h4>
            </Link>
          </div>
          <Divider className="my-4" />
          <div className="space-y-1">
           <Link to="/usersList" color="foreground">
            <h4 className="text-medium font-medium">Manage users</h4>
           </Link>
          </div>
          <Divider className="my-4" />
          <Logout />
        </>
      ) : (
        <>
          <div className="space-y-1">
            <Link to="/" color="foreground">
              <h4 className="text-medium font-medium">Animals</h4>
            </Link>
          </div>
          <Divider className="my-4" />
          <div className="space-y-1">
            <Link to="/allvaccinations" color="foreground">
              <h4 className="text-medium font-medium">Vaccinations</h4>
            </Link>
          </div>
          <Divider className="my-4" />
          <Logout />
        </>
      )}
    </div>
  );
};

export default SideBarContent;
