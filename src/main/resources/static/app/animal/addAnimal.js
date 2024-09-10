import React, {useState} from 'react';
import {gql} from "graphql-tag";
import {useMutation} from "@apollo/client";
import showError from "./showError";
import { ANIMALS_QUERY } from '../graphqlQueries.js';

const ADD_ANIMAL = gql`
    mutation ($name: String!,
        $species: String!,
        $primary_color: String!,
        $breed: String,
        $implant_chip_id: String,
        $gender: String!,
        $birth_date: String,
        $pattern: String)
    {
        addAnimal(name: $name,
            species: $species,
            primary_color:  $primary_color,
            breed: $breed,
            implant_chip_id: $implant_chip_id,
            gender: $gender,
            birth_date:  $birth_date,
            pattern: $pattern)
        {
            id
            name
            species
            primary_color
            breed
            implant_chip_id
            gender
            birth_date
            pattern
        }
    }
`;

function AddAnimal() {

    const initialValues = {
        name: "",
        species: "Dog",
        primary_color: "White",
        gender: "F",
        breed: "",
        implant_chip_id: "11111111-1111-1111-1111-111111111111",
        birth_date: "2012-01-01 00:00:00.0",
        pattern: "Broken"
    };

    const [animal, setAnimal] = useState(initialValues);
    const [validationError, setError] = useState(null);
    const [addAnimal] = useMutation(ADD_ANIMAL, {
       update(cache, { data: { addAnimal } }) {
         try {
           const { allAnimals } = cache.readQuery({ query: ANIMALS_QUERY });
           cache.writeQuery({
             query: ANIMALS_QUERY,
             data: { allAnimals: [...allAnimals, addAnimal] },
           });
         } catch (error) {
           console.error("Error updating cache:", error);
         }
       }
     });


    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setAnimal({
            ...animal,
            [name]: value,
        });
    };

    const clearFields = () => {
         setAnimal(initialValues);
    }

    return (
       <tr>
            <td></td>
            <td><input name="name" value={animal.name}
                   onChange={handleInputChange}
                   placeholder={validationError === 'name' ? 'Name is mandatory' : ''} />
            </td>
            <td><input name="species" value={animal.species}
                   onChange={handleInputChange}
                   placeholder={validationError === 'species' ? 'Species is mandatory' : ''} /></td>
            <td>
             <select
                   name="primary_color"
                   value={animal.primary_color}
                   onChange={handleInputChange}
                   placeholder={validationError === 'primary_color' ? 'Primary color is mandatory' : ''} >
                       <option value="White">White</option>
                       <option value="Black">Black</option>
                       <option value="Red">Red</option>
                       <option value="Multi">Multi</option>
             </select>
            </td>
            <td><input name="breed" value={animal.breed} onChange={handleInputChange}/></td>
            <td><input className="long" name="implant_chip_id" value={animal.implant_chip_id}
                   onChange={handleInputChange}
                   placeholder={validationError === 'implant_chip_id' ? 'implant_chip_id is mandatory' : ''}/>
            </td>
            <td>
             <select
                   name="gender"
                   value={animal.gender}
                   onChange={handleInputChange}
                   placeholder={validationError === 'gender' ? 'Gender is mandatory' : ''} >
                      <option value="F">F</option>
                      <option value="M">M</option>
             </select>
            </td>
            <td><input name="birth_date" value={animal.birth_date} onChange={handleInputChange}/></td>
            <td><input name="pattern" value={animal.pattern} onChange={handleInputChange}/></td>
            <td>
                <button className="button" onClick={
                    function () {
                         if (!animal.name) {
                              setError('name');
                              return;
                         }
                         if (!animal.species) {
                               setError('species');
                               return;
                          }
                         if (!animal.implant_chip_id) {
                               setError('implant_chip_id');
                               return;
                         }
                         if (!animal.primary_color) {
                               setError('primary_color');
                               return;
                         }
                         if (!animal.gender) {
                               setError('gender');
                               return;
                         }

                        addAnimal(
                            {
                                variables: {
                                    name: animal.name,
                                    species: animal.species,
                                    primary_color: animal.primary_color,
                                    breed: animal.breed,
                                    implant_chip_id: animal.implant_chip_id,
                                    gender: animal.gender,
                                    birth_date: animal.birth_date,
                                    pattern: animal.pattern
                                }
                            }).catch( (error1) => { showError( {error: error1})});

                        clearFields();
                    }} className="round-button-with-border">
                      Add
                </button>
            </td>
        </tr>
    );
}

export default AddAnimal;
