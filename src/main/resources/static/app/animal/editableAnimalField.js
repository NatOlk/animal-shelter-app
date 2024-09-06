import React, { useState } from 'react';
import { useMutation } from '@apollo/client';
import { gql } from 'graphql-tag';
import DeleteAnimal from './deleteAnimal';
import { ANIMALS_QUERY } from '../graphqlQueries.js';

const UPDATE_ANIMAL = gql`
    mutation UpdateAnimal($name: String!,
                          $species: String!,
                          $primary_color: String,
                          $breed: String,
                          $gender: String,
                          $birth_date: String,
                          $pattern: String) {
        updateAnimal(name: $name, species: $species,
                     primary_color: $primary_color,
                     breed: $breed,
                     gender: $gender,
                     birth_date: $birth_date,
                     pattern: $pattern) {
                          name
                          species
                          primary_color
                          breed
                          gender
                          birth_date
                          pattern
        }
    }
`;

const EditableAnimalField = ({ animal, value, name, values }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [fieldValue, setFieldValue] = useState(value);
  const [oldValue, setOldValue] = useState("");
  const [fieldValues, setFieldValues] = useState(values);

 const [updateField, { loading, error }] = useMutation(UPDATE_ANIMAL, {
    refetchQueries: [ANIMALS_QUERY],
    onCompleted: () => setIsEditing(false),
  });

  const handleSave = () => {
    const variables = {
      [name]: fieldValue,
      name: animal.name,
      species: animal.species
    }
    updateField({ variables });
  };

 const inputStyle = isEditing ? { backgroundColor: '#FFC0CB' } : {};

   return (
     <td>
       {isEditing ? (
         values && values.length > 0 ? (
           <select
             style={inputStyle}
             value={fieldValue}
             onChange={(e) => setFieldValue(e.target.value)}
           >
             {values.map((val) => (
               <option key={val} value={val}>
                 {val}
               </option>
             ))}
           </select>
         ) : (
           <input
             style={inputStyle}
             value={fieldValue}
             onChange={(e) => setFieldValue(e.target.value)}
           />
         )
       ) :  (
                    values && values.length > 0 ? (
                      <select
                        style={inputStyle}
                        value={fieldValue}
                        readOnly
                        onDoubleClick={() => {
                                    setOldValue(fieldValue);
                                    setIsEditing(true);
                                  }}
                      >
                        {values.map((val) => (
                          <option key={val} value={val}>
                            {val}
                          </option>
                        ))}
                      </select>
                    ) : (
         <input
           style={inputStyle}
           value={fieldValue}
           readOnly
           onDoubleClick={() => {
             setOldValue(fieldValue);
             setIsEditing(true);
           }}
         />
       ))}
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