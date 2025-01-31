import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AnimalsList from "./animal/animalsList.jsx";
import AnimalDetails from "./animal/animalDetails.jsx";
import VaccinationsList from "./vaccination/vaccinationsList.jsx";
import SideBarContent from './sideBarContent.jsx';
import NavBarContent from './navBarContent.jsx';
import AllVaccinationsList from "./vaccination/allVaccinationsList.jsx";
import UserProfile from './account/userProfile.jsx';
import { ConfigProvider } from './common/configContext.jsx';
import Login from "./login.jsx";
import Logout from "./logout.jsx";
import ProtectedRoute from "./common/protectedRoute.jsx";
import ErrorPage from "./errorPage.jsx";
import { AuthProvider, useAuth } from './common/authContext.jsx';
import QuickSubscribe from './account/quickSubscribe.jsx';
import { Spacer } from "@nextui-org/react";
import ErrorBoundary from './common/errorBoundary.jsx';
import './styles/global.css';

const AppContent = () => {
  const { isAuthenticated } = useAuth();

  return (
    <>
      <div className="navbar-container">
        <NavBarContent />
      </div>

      <div className="main-container">
        {isAuthenticated && <aside className="sidebar">
          <SideBarContent />
        </aside>}

        <main className="content">
          <ErrorBoundary>
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
              <Route path="/animals/:id" element={<AnimalDetails />} />
            </Routes>
          </ErrorBoundary>
        </main>
      </div>
      <Spacer y={10} />
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
