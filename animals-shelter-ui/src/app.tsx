import React, { FC } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AnimalsList from "./animal/animalsList";
import AnimalDetails from "./animal/animalDetails";
import VaccinationsList from "./vaccination/vaccinationsList";
import SideBarContent from './sideBarContent';
import NavBarContent from './navBarContent';
import AllVaccinationsList from "./vaccination/allVaccinationsList";
import UserProfile from './account/userProfile';
import UsersList from './account/usersList';
import { ConfigProvider } from './common/configContext';
import Login from "./login";
import Logout from "./logout";
import RegistrationForm from "./registrationForm";
import ProtectedRoute from "./common/protectedRoute";
import ErrorPage from "./errorPage";
import { AuthProvider, useAuth } from './common/authContext';
import QuickSubscribe from './subscription/quickSubscribe';
import StatisticsDashboard from './stats/statisticsDashboard';
import { Spacer } from "@nextui-org/react";
import ErrorBoundary from './common/errorBoundary';
import './styles/global.css';

const AppContent: FC = () => {
  const { isAuthenticated, isAdmin } = useAuth();

  return (
    <>
      <div className="navbar-container">
        <NavBarContent />
      </div>

      <div className="main-container">
        {isAuthenticated && <aside className="sidebar">
          <ProtectedRoute>
            <SideBarContent />
          </ProtectedRoute>
        </aside>}

        <main className="content">
          <ErrorBoundary>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/logout" element={<Logout />} />
              <Route path="/register" element={<RegistrationForm />} />
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
              <Route path="/vaccinations/:animalId" element={
                <ProtectedRoute>
                  <VaccinationsList />
                </ProtectedRoute>
              } />
              <Route path="/allvaccinations" element={
                <ProtectedRoute>
                  <AllVaccinationsList />
                </ProtectedRoute>
              } />
              <Route path="/animals/:id" element={
                <ProtectedRoute>
                  <AnimalDetails />
                </ProtectedRoute>
              } />
              <Route path="/usersList" element={
                <ProtectedRoute>
                  <UsersList />
                </ProtectedRoute>
              } />
              <Route path="/statistics" element={
                <ProtectedRoute>
                 <StatisticsDashboard />
                </ProtectedRoute>
              } />
            </Routes>
          </ErrorBoundary>
        </main>
      </div>

      <Spacer y={10} />
      {isAuthenticated && isAdmin && <QuickSubscribe />}
    </>
  );
};

const App: FC = () => {
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
