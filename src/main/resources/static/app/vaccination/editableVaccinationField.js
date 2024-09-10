import React, { useState } from 'react';
import { useMutation } from '@apollo/client';
import { gql } from 'graphql-tag';
import DeleteVaccination from './deleteVaccination';
import { VACCINATIONS_QUERY } from '../graphqlQueries.js';

const UPDATE_VACCINATION = gql`
    mutation UpdateVaccination($id: ID!,
                          $vaccine: String!,
                          $batch: String!,
                          $vaccination_time: String,
                          $comments: String,
                          $email: String) {
        updateVaccination(id: $id,
                     vaccine: $vaccine,
                     batch: $batch,
                     vaccination_time: $vaccination_time,
                     comments: $comments,
                     email: $email) {
                          id
                          vaccine
                          batch
                          vaccination_time
                          comments
                          email
        }
    }
`;

const EditableVaccineField = ({ vaccination, value, name }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [fieldValue, setFieldValue] = useState(value || "");
  const [oldValue, setOldValue] = useState("");

 const [updateField, { loading, error }] = useMutation(UPDATE_VACCINATION, {
    refetchQueries: [VACCINATIONS_QUERY],
    onCompleted: () => setIsEditing(false),
  });

  const handleSave = () => {
    const variables = {
      [name]: fieldValue,
      id: vaccination.id,
      vaccine: vaccination.vaccine,
      batch: vaccination.batch,
      vaccination_time: vaccination.vaccination_time,
      comments: vaccination.comments,
      email: vaccination.email
    }
    updateField({ variables });
  };

 const inputStyle = isEditing ? { backgroundColor: '#FFC0CB' } : {};
  return (
    <td>
      <input
        style={inputStyle}
        value={fieldValue}
        onDoubleClick={(e) => {
                     setOldValue(e.target.value);
                     setIsEditing(true);
                  }}
        onChange={(e) => setFieldValue(e.target.value)}
      />
      {isEditing && (
        <>
          <button className="round-button-with-border" onClick={handleSave}>+</button>
          <button className="round-button-with-border" onClick={() => { setFieldValue(oldValue);
                                                                       setIsEditing(false);}}>-</button>
        </>
      )}
    </td>
  )
};

export default EditableVaccineField;