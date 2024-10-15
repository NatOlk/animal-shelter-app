import React, { useState, useEffect } from 'react';
import { useMutation } from '@apollo/client';
import axios from 'axios';
import DeleteVaccination from './deleteVaccination';
import EditableVaccinationField from './editableVaccinationField';
import useConfig from '../common/useConfig';

const UpdateVaccination = ({ vaccination }) => {
  const [vaccines, setVaccines] = useState([]);

  const config = useConfig();
  if (!config) {
    return (
      <tr>
        <td colSpan="5">Loading animals configs...</td>
      </tr>
    );
  };
  return (
    <tr key={vaccination.id}>
      <td style={{ whiteSpace: 'nowrap' }}>{vaccination.id}</td>
      <EditableVaccinationField
        vaccination={vaccination}
        value={vaccination.vaccine}
        name="vaccine"
        values={config.vaccines}/>
      <td>{vaccination.batch}</td>
      <EditableVaccinationField
        vaccination={vaccination}
        value={vaccination.vaccinationTime}
        name="vaccinationTime"/>
      <EditableVaccinationField
        vaccination={vaccination}
        value={vaccination.comments}
        name="comments"/>
      <EditableVaccinationField
        vaccination={vaccination}
        value={vaccination.email}
        name="email"/>
      <td>
        <DeleteVaccination id={vaccination.id}/>
      </td>
    </tr>
  );
}

export default UpdateVaccination;
