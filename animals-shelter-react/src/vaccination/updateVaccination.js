import React from 'react';
import DeleteVaccination from './deleteVaccination';
import EditableVaccinationField from './editableVaccinationField';
import { useConfig } from '../common/configContext';

const UpdateVaccination = ({ vaccination }) => {

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
        values={config.config.vaccines} />
      <td>{vaccination.batch}</td>
      <EditableVaccinationField
        vaccination={vaccination}
        value={vaccination.vaccinationTime}
        name="vaccinationTime"
        isDate={true}/>
      <EditableVaccinationField
        vaccination={vaccination}
        value={vaccination.comments}
        name="comments" />
      <EditableVaccinationField
        vaccination={vaccination}
        value={vaccination.email}
        name="email" />
      <td>
        <DeleteVaccination id={vaccination.id} />
      </td>
    </tr>
  );
}

export default UpdateVaccination;
