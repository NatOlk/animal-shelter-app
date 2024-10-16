import React, { useState } from 'react';
import { useMutation } from '@apollo/client';
import { VACCINATIONS_QUERY, UPDATE_VACCINATION } from '../common/graphqlQueries.js';

const EditableVaccineField = ({ vaccination, value, name, values }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [fieldValue, setFieldValue] = useState(value || "");
  const [oldValue, setOldValue] = useState("");

  const [updateField, { loading, error }] = useMutation(UPDATE_VACCINATION, {
    refetchQueries: [VACCINATIONS_QUERY],
    onCompleted: () => setIsEditing(false),
  });

  const handleSave = () => {
    const variables = {
      id: vaccination.id,
      [name]: fieldValue
    };
    updateField({ variables }).catch(err => console.error(err));
  };

  const inputStyle = isEditing ? { backgroundColor: '#FFC0CB' } : {};

  return (
    <td>
      {values && values.length > 0 ? (
        isEditing ? (
          <select
            style={inputStyle}
            value={fieldValue}
            className="browser-default"
            onChange={(e) => setFieldValue(e.target.value)}
          >
            {values.map((val) => (
              <option key={val} value={val}>
                {val}
              </option>
            ))}
          </select>
        ) : (
          <select
            style={inputStyle}
            value={fieldValue}
            className="browser-default"
            readOnly
            onDoubleClick={() => {
              setOldValue(fieldValue);
              setIsEditing(true);
            }}>
            {values.map((val) => (
              <option key={val} value={val}>
                {val}
              </option>
            ))}
          </select>
        )
      ) : (
        <input
          style={inputStyle}
          value={fieldValue}
          onDoubleClick={() => {
            setOldValue(fieldValue);
            setIsEditing(true);
          }}
          onChange={(e) => setFieldValue(e.target.value)}
        />
      )}
      {isEditing && (
        <>
          <button className="round-button-with-border" onClick={handleSave}>+</button>
          <button className="round-button-with-border" onClick={() => {
            setFieldValue(oldValue);
            setIsEditing(false);
          }}>-</button>
        </>
      )}
    </td>
  );
};

export default EditableVaccineField;
