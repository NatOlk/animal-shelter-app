import React, { useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import AnimalsList from "./animal/animalsList";
import VaccinationsList from "./vaccination/vaccinationsList";
import SideBarContent from './sideBarContent';
import AllVaccinationsList from "./vaccination/allVaccinationsList";
import UserProfile from './account/userProfile';
import { ConfigProvider } from './common/configContext';
import Login from "./login";
import Logout from "./logout";
import ProtectedRoute from "./common/protectedRoute";
import ErrorPage from "./errorPage";
import { AuthProvider, useAuth } from './common/authContext';
import M from 'materialize-css';
import 'materialize-css/dist/css/materialize.min.css';
import 'materialize-css/dist/js/materialize.min.js';

const AppContent = () => {
  const { isAuthenticated } = useAuth();
  return (
    <>
      <nav>
        <div className="nav-wrapper headbackgroud">
          <a href="/" className="brand-logo">Animal Shelter</a>
          {isAuthenticated && (
            <ul id="nav-mobile" className="right hide-on-med-and-down">
              <li>
                <img src="../img/logo.jpg" className="circle responsive-img" alt=""></img>
              </li>
               <li>
                  <Link to="/profile">Profile</Link>
               </li>
            </ul>
          )}
        </div>
      </nav>

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
            <Route path="/profile" element={<UserProfile />} />
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
  useEffect(() => {
    const elems = document.querySelectorAll('select');
    M.FormSelect.init(elems);
    const elems2 = document.querySelectorAll('.datepicker');
    M.Datepicker.init(elems2);
    const elems3 = document.querySelectorAll('.modal');
    M.Modal.init(elems3);
    return () => {
      M.FormSelect.destroy(elems);
      M.Datepicker.destroy(elems2);
      M.Modal.destroy(elems3);
    };
  }, []);

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
