import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useQuery, useMutation } from '@apollo/client';
import axios from 'axios';
import DeleteAnimal from './deleteAnimal';
import EditableAnimalField from './editableAnimalField';
import { useConfig } from '../common/configContext';

const UpdateAnimal = ({ animal }) => {
  const [genders, setGenders] = useState([]);

  const config = useConfig();
  if (!config) {
    return (
      <tr>
        <td colSpan="5">Loading animals configs...</td>
      </tr>
    );
  };
  return (
    <tr key={animal.id}>
      <td style={{ whiteSpace: 'nowrap' }}>{animal.id}</td>
      <td style={{ whiteSpace: 'nowrap' }}>{animal.name}</td>
      <td>{animal.species}</td>
      <EditableAnimalField
        animal={animal}
        value={animal.primaryColor}
        name="primaryColor"
        values={config.config.colors}/>
      <EditableAnimalField
        animal={animal}
        value={animal.breed}
        name="breed"/>
      <td>{animal.implantChipId}</td>
      <EditableAnimalField
        animal={animal}
        value={animal.gender}
        name="gender"
        values={config.genders}/>
      <EditableAnimalField
        animal={animal}
        value={animal.birthDate}
        name="birthDate"/>
      <EditableAnimalField
        animal={animal}
        value={animal.pattern}
        name="pattern"/>
      <td>
        <DeleteAnimal id={animal.id} />
        <Link
          className="waves-effect waves-light btn-small"
          to={`/vaccinations`}
          state={{ animalId: animal.id, name: animal.name, species: animal.species }}>
          Vax
          {animal.vaccinationCount !== 0 && (
            <span> ({animal.vaccinationCount})</span>
          )}
        </Link>
      </td>
    </tr>
  );
};

export default UpdateAnimal;
