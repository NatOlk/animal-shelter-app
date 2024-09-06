import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import AnimalsList from "./animal/AnimalsList";
import VaccinationsList from "./vaccination/VaccinationsList";
import LeftContainer from "./LeftContainer";
import RightContainer from "./RightContainer";
import Subscription from "./animal/subscription";


const SidebarContent = () => (
  <>
    <h2>Navigation</h2>
    <ul>
      <li>
        <Link to="/">Animals List</Link>
      </li>
    </ul>
  </>
);

const App = () => (
  <Router>
    <table className="container">
      <tbody>
        <tr>
          <td className="left">
            <SidebarContent />
            <Subscription />
          </td>
          <td>
            <Routes>
              <Route path="/" element={<AnimalsList />} />
              <Route path="/vaccinations" element={<VaccinationsList />} />
            </Routes>
          </td>
        </tr>
      </tbody>
    </table>
  </Router>
);

export default App;
