import React from "react";
import { useAuth } from './common/authContext';
import { Link } from "react-router-dom";

const NavBarContent = () => {
  const { isAuthenticated } = useAuth();
  return (
    <>
      <nav>
        <div className="nav-wrapper headbackgroud">
          <a href="/" className="brand-logo">&nbsp;&nbsp;&nbsp;Animal Shelter Hippo</a>
          {isAuthenticated && (
            <ul id="nav-mobile" className="right hide-on-med-and-down">
              <li>
                <img src="../img/logo2.jpg" className="circle responsive-img" alt=""></img>
              </li>
              <li>
                <Link to="/profile"><h5>Profile</h5>&nbsp;&nbsp;&nbsp;</Link>
              </li>
            </ul>
          )}
        </div>
      </nav>
    </>
  );
};

export default NavBarContent;
