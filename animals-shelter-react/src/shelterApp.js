import React from "react";

import { ConfigProvider } from './common/configContext';
import Login from "./login";
import Logout from "./logout";
import { AuthProvider, useAuth } from './common/authContext';


const ShelterApp = () => {
  return (
    <AuthProvider>
        <ConfigProvider>
         <DropdownItem href="/login" routerOptions={{replace: true}}>
           {/* ...*/}
         </DropdownItem>
        </ConfigProvider>
    </AuthProvider>
  );
};

export default ShelterApp;