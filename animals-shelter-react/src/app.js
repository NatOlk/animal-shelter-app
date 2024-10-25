import React, { useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
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
import { AuthProvider } from './common/authContext';
import M from 'materialize-css';
import 'materialize-css/dist/css/materialize.min.css';
import 'materialize-css/dist/js/materialize.min.js';

const AppContent = () => {
  return (
    <>
      <NavBarContent />
      <div className="valign-wrapper">
        <h1> </h1>
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
