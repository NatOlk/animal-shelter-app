import React, { useState } from 'react';
import { useMutation } from '@apollo/client';
import { gql } from 'graphql-tag';
import DeleteVaccination from './deleteVaccination';
import EditableVaccinationField from './editableVaccinationField';

const UpdateVaccination = ({ vaccination }) => {

  return (
    <tr key={vaccination.id}>
      <td style={{whiteSpace: 'nowrap'}}>{vaccination.id}</td>
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
        <DeleteVaccination id={vaccination.id}/>
      </td>
    </tr>
  )
}

export default UpdateVaccination;