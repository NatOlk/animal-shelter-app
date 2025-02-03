import React from "react";
import { useAuth } from './common/authContext';
import { Navbar, NavbarBrand, NavbarContent, NavbarItem, Image, Button } from "@nextui-org/react";
import { useNavigate } from 'react-router-dom';

export const Logo: React.FC = () => {
  return (
    <Image src="./img/hippologo.png" alt="Logo" />
  );
};

const NavBarContent: React.FC = () => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleClick = (): void => {
    navigate('/profile');
  };

  return (
    <Navbar height="90px" isBordered>
      <NavbarBrand>
        <Logo />
      </NavbarBrand>
      <NavbarContent justify="end">
        {isAuthenticated && (
          <NavbarItem>
            <Button color="default" variant="flat" onPress={handleClick}>
              <h5>Profile</h5>
            </Button>
          </NavbarItem>
        )}
      </NavbarContent>
    </Navbar>
  );
};

export default NavBarContent;
