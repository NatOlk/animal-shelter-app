import React, {useState} from 'react';
import {gql} from "graphql-tag";
import {useMutation} from "@apollo/client";
import showError from "./showError";
import { VACCINATIONS_QUERY } from '../graphqlQueries.js';

const ADD_VACCINATION = gql`
    mutation ($name: String!,
        $species: String!,
        $vaccine: String!,
        $batch: String!,
        $vaccination_time: String!,
        $comments: String,
        $email: String!)
    {
        addVaccination(name: $name,
            species: $species,
            vaccine:  $vaccine,
            batch: $batch,
            vaccination_time: $vaccination_time,
            comments: $comments,
            email:  $email)
        {
            name
            species
            vaccine
            batch
            vaccination_time
            comments
            email
        }
    }
`;

function AddVaccination({name, species}) {

    const [vaccination, setVaccination] = useState({
        name: name,
        species: species,
        vaccine: 'Rabies',
        batch: 'RTL-0001',
        vaccination_time: new Date().toISOString().split('.')[0],
        comments: 'Add new vaccine',
        email: 'nolkoeva@gmail.com'
      });
    const [validationError, setError] = useState(null);
    const [addVaccination, {error , loading, data}] = useMutation(ADD_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY]
    });

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setVaccination({
            ...vaccination,
            [name]: value,
        });
    };

    const clearFields = () => {
          setVaccination({
                name: name,
                species: species,
                vaccine: 'Rabies',
                batch: 'RTL-0001',
                vaccination_time: new Date().toISOString().split('.')[0],
                comments: 'Add new vaccine',
                email: 'nolkoeva@gmail.com'
            });
    }

    return (
       <tr>
            <td><input name="name" value={name} readOnly/>
            </td>
            <td><input name="species" value={species} readOnly/></td>
            <td><input name="vaccine" value={vaccination.vaccine}
                   onChange={handleInputChange}
                   placeholder={validationError === 'vaccine' ? 'Vaccine is mandatory' : ''}/></td>
            <td><input name="batch" value={vaccination.batch}
                   onChange={handleInputChange}
                   placeholder={validationError === 'batch' ? 'batch is mandatory' : ''}/></td>
            <td><input className="long" name="vaccination_time" value={vaccination.vaccination_time}
                   onChange={handleInputChange}
                   placeholder={validationError === 'vaccination_time' ? 'vaccination_time is mandatory' : ''}/>
            </td>
            <td><input name="comments" value={vaccination.comments}
                   onChange={handleInputChange} /></td>
            <td><input name="email" value={vaccination.email} onChange={handleInputChange}/></td>
            <td>
                <button className="button" onClick={
                    function () {

                         if (!vaccination.vaccine) {
                               setError('vaccine');
                               return;
                         }
                         if (!vaccination.batch) {
                               setError('batch');
                               return;
                         }
                         if (!vaccination.vaccination_time) {
                               setError('vaccination_time');
                               return;
                         }

                        addVaccination(
                            {
                                variables: {
                                    name: vaccination.name,
                                    species: vaccination.species,
                                    vaccine: vaccination.vaccine,
                                    batch: vaccination.batch,
                                    vaccination_time: vaccination.vaccination_time,
                                    comments: vaccination.comments,
                                    email: vaccination.email
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

export default AddVaccination;
