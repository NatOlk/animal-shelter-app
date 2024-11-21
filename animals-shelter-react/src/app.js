import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AnimalsList from "./animal/animalsList";
import VaccinationsList from "./vaccination/vaccinationsList";
import SideBarContent from './sideBarContent';
import NavBarContent from './navBarContent';
import AllVaccinationsList from "./vaccination/allVaccinationsList";
import UserProfile from './account/userProfile';
import { ConfigProvider } from './common/configContext';
import Login from "./login";
import Logout from "./logout";
import ProtectedRoute from "./common/protectedRoute";
import ErrorPage from "./errorPage";
import { AuthProvider, useAuth } from './common/authContext';
import QuickSubscribe from './account/quickSubscribe';

const AppContent = () => {
  const { isAuthenticated } = useAuth();

  return (
    <>
      <div className="row">
        <div className="col s12">
          <NavBarContent />
        </div>
      </div>

      <div className="row">
        <div className="col s2">
          <SideBarContent />
        </div>
        <div className="col s10">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/logout" element={<Logout />} />
            <Route path="/error" element={<ErrorPage />} />
            <Route path="/profile" element={
              <ProtectedRoute>
                <UserProfile />
              </ProtectedRoute>
            } />
            <Route path="/" element={
              <ProtectedRoute>
                <AnimalsList />
              </ProtectedRoute>
            } />
            <Route path="/vaccinations" element={
              <ProtectedRoute>
                <VaccinationsList />
              </ProtectedRoute>
            } />
            <Route path="/allvaccinations" element={
              <ProtectedRoute>
                <AllVaccinationsList />
              </ProtectedRoute>
            } />
          </Routes>
        </div>
      </div>
      {isAuthenticated && <QuickSubscribe />}
    </>
  );
};

const App = () => {
  return (
    <AuthProvider>
      <Router>
        <ConfigProvider>
          <AppContent />
        </ConfigProvider>
      </Router>
    </AuthProvider>
  );
};

export default App;
