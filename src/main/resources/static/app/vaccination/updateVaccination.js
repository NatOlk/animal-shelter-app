import React, { useState } from 'react';
import { useMutation } from '@apollo/client';
import { gql } from 'graphql-tag';
import DeleteVaccination from './deleteVaccination';
import EditableVaccinationField from './editableVaccinationField';

const UpdateVaccination = ({ vaccination }) => {

  return (
    <tr key={vaccination.name + vaccination.species + vaccination.vaccine + vaccination.batch}>
      <td style={{whiteSpace: 'nowrap'}}>{vaccination.name}</td>
      <td>{vaccination.species}</td>
      <td>{vaccination.vaccine}</td>
      <td>{vaccination.batch}</td>
      <EditableVaccinationField
              vaccination={vaccination}
              value={vaccination.vaccination_time}
              name="vaccination_time"/>
      <EditableVaccinationField
                  vaccination={vaccination}
                  value={vaccination.comments}
                  name="comments"/>
      <EditableVaccinationField
                  vaccination={vaccination}
                  value={vaccination.email}
                  name="email"/>
      <td>
        <DeleteVaccination vaccination={vaccination}/>
      </td>
    </tr>
  )
}

export default UpdateVaccination;