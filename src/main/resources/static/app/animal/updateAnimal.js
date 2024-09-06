import React, { useState, useEffect } from 'react';
import { useMutation } from '@apollo/client';
import { gql } from 'graphql-tag';
import DeleteAnimal from './deleteAnimal';
import EditableAnimalField from './editableAnimalField';
import { Link } from 'react-router-dom';
import { GET_VACCINATION_COUNT } from '../graphqlQueries.js';

const genders = ["M", "F"];
const colors = ["White", "Black", "Red"];

const UpdateAnimal = ({ animal }) => {
 const { data, loading, error } = useQuery(GET_VACCINATION_COUNT, {
    variables: { id: animal.id },
  });
  if (loading) return <p>Загрузка...</p>;
  if (error) return <p>Ошибка загрузки данных: {error.message}</p>;
  const vaccinationCount = data?.animal?.vaccinationCount || 0;

  return (
    <tr key={animal.name + animal.species}>
      <td style={{whiteSpace: 'nowrap'}}>{animal.name}</td>
      <td>{animal.species}</td>
      <EditableAnimalField
              animal={animal}
              value={animal.primary_color}
              name="primary_color"
              values={colors}
            />
     <EditableAnimalField
                    animal={animal}
                    value={animal.breed}
                    name="breed"
                  />
     <td>{animal.implant_chip_id}</td>
     <EditableAnimalField
                     animal={animal}
                     value={animal.gender}
                     name="gender"
                     values={genders}
                   />
      <EditableAnimalField
                  animal={animal}
                  value={animal.birth_date}
                  name="birth_date"
                />
      <EditableAnimalField
                   animal={animal}
                   value={animal.pattern}
                   name="pattern"
                  />
      <td>
       <DeleteAnimal name={animal.name} species={animal.species}/>
       <Link
             className="round-button-with-border"
             to={`/vaccinations`}
             state={{ name: animal.name, species: animal.species }}
           >
             Vaccinations
             {vaccinationCount !== null && (
               <span> ({vaccinationCount})</span>
             )}
           </Link>
      </td>
    </tr>
  )
}

export default UpdateAnimal;