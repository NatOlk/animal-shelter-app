import React from 'react';
import { Link } from 'react-router-dom';
import DeleteAnimal from './deleteAnimal';
import EditableAnimalField from './editableAnimalField';
import { useConfig } from '../common/configContext';

const UpdateAnimal = ({ animal }) => {

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
        values={config.config.colors} />
      <EditableAnimalField
        animal={animal}
        value={animal.breed}
        name="breed" />
      <td>{animal.implantChipId}</td>
      <EditableAnimalField
        animal={animal}
        value={animal.gender}
        name="gender"
        values={config.config.genders} />
      <EditableAnimalField
        animal={animal}
        value={animal.birthDate}
        name="birthDate"
        isDate={true}/>
      <EditableAnimalField
        animal={animal}
        value={animal.pattern}
        name="pattern" />
      <td>
        <DeleteAnimal id={animal.id} />&nbsp;
        <Link
          className="waves-effect waves-orange btn-small"
          to={`/vaccinations`}
          state={{ animalId: animal.id, name: animal.name, species: animal.species }}>
          <i className="small material-icons">vaccines</i>
            <span className="font12"> {animal.vaccinationCount}</span>
        </Link>
      </td>
    </tr>
  );
};

export default UpdateAnimal;
