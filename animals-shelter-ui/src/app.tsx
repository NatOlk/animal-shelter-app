import React, { FC } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AnimalPublicDetails from "./animal/animalPublicDetails";
import AppPrivateContent from "./appPrivate";

const App: FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/public/animals/:id" element={<AnimalPublicDetails />} />
        <Route path="/*" element={<AppPrivateContent />} />
      </Routes>
    </Router>
  );
};

export default App;
