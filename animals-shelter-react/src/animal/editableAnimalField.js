import React, { useState, useEffect } from 'react';
import { useMutation } from '@apollo/client';
import M from 'materialize-css';
import { ANIMALS_QUERY, UPDATE_ANIMAL } from '../common/graphqlQueries.js';

const EditableAnimalField = ({ animal, value, name, values, isDate }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [fieldValue, setFieldValue] = useState(value);
  const [oldValue, setOldValue] = useState("");

  const [updateField, { loading, error }] = useMutation(UPDATE_ANIMAL, {
    refetchQueries: [ANIMALS_QUERY],
    onCompleted: () => setIsEditing(false),
  });

  useEffect(() => {
    if (isDate && isEditing) {
      const datepickerElem = document.querySelector(`#${name}-${animal.id}`);
      const instance = M.Datepicker.init(datepickerElem, {
        format: 'yyyy-mm-dd',
        onSelect: (date) => {
          setFieldValue(date.toISOString().slice(0, 10));
        },
      });
      return () => instance && instance.destroy();
    }
  }, [isDate, isEditing]);

  const handleSave = () => {
    const variables = {
      id: animal.id,
      [name]: fieldValue,
    };

    updateField({ variables }).catch(err => console.error(err));
  };

  const inputStyle = isEditing ? `red-background` : {};
  const combinedClassName = `${inputStyle} browser-default`;

  return (
    <td>
      {isEditing ? (
        values && values.length > 0 ? (
          <select
            value={fieldValue}
            className={combinedClassName}
            onChange={(e) => setFieldValue(e.target.value)}
          >
            {values.map((val) => (
              <option key={val} value={val}>
                {val}
              </option>
            ))}
          </select>
        ) : isDate ? (
          <input
            type="text"
            id={`${name}-${animal.id}`}
            className={`${combinedClassName} datepicker`}
            value={fieldValue}
            onChange={(e) => setFieldValue(e.target.value)}
          />
        ) : (
          <input
            className={combinedClassName}
            value={fieldValue}
            onChange={(e) => setFieldValue(e.target.value)}
          />
        )
      ) : (
        <span
          onDoubleClick={() => {
            setOldValue(fieldValue);
            setIsEditing(true);
          }}
        >
          {fieldValue}
        </span>
      )}
      {isEditing && (
        <>
          <button
            className="round-button-with-border"
            onClick={handleSave}
          >
            +
          </button>
          <button
            className="round-button-with-border"
            onClick={() => {
              setFieldValue(oldValue);
              setIsEditing(false);
            }}
          >
            -
          </button>
        </>
      )}
    </td>
  );
};

export default EditableAnimalField;
