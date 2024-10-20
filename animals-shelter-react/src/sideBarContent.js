import React from "react";
import { useAuth } from './common/authContext';
import { Link } from "react-router-dom";
import Logout from './logout';

const SideBarContent = () => {
  const { isAuthenticated } = useAuth();

  return (
    <>
      {isAuthenticated && (
        <div className="collection">
          <Link to="/"
            className="collection-item round-button-with-border">
            <h6>Animals</h6>
          </Link>
          <Link to="/allvaccinations"
            className="collection-item round-button-with-border">
            <h6>Vaccinations</h6>
          </Link>
          <div className="collection-item">
            <Logout />
          </div>
        </div>
      )}
    </>
  );
};

export default SideBarContent;
