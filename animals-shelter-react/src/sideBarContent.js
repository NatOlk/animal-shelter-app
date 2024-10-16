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
          <Link to="/" className="collection-item round-button-with-border"><p>Animals</p></Link>
          <Link to="/allvaccinations" className="collection-item round-button-with-border"><p>Vaccinations</p></Link>
          <div className="collection-item">
            <Logout />
          </div>
        </div>
      )}
    </>
  );
};

export default SideBarContent;
