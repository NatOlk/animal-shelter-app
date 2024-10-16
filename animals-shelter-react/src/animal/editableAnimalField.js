import React, { useState } from 'react';
import { useMutation } from '@apollo/client';
import DeleteAnimal from './deleteAnimal';
import { ANIMALS_QUERY, UPDATE_ANIMAL} from '../common/graphqlQueries.js';

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
        id: animal.id,
        [name]: fieldValue,
      };

      updateField({ variables }).catch(err => console.error(err));
    };

 const inputStyle = isEditing ? { backgroundColor: '#FFC0CB' } : {};

   return (
     <td>
       {isEditing ? (
         values && values.length > 0 ? (
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
                        className="browser-default"
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