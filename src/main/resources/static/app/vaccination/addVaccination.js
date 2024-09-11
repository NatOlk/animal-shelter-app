import React, { useState, useEffect } from 'react';
import { gql } from "graphql-tag";
import { useMutation } from "@apollo/client";
import axios from 'axios';
import showError from "./showError";
import useConfig from '../useConfig';
import { VACCINATIONS_QUERY } from '../graphqlQueries.js';

const ADD_VACCINATION = gql`
    mutation (
        $animalId: ID!,
        $vaccine: String!,
        $batch: String!,
        $vaccination_time: String!,
        $comments: String,
        $email: String!)
    {
        addVaccination(
            animalId: $animalId,
            vaccine:  $vaccine,
            batch: $batch,
            vaccination_time: $vaccination_time,
            comments: $comments,
            email:  $email)
        {
            id
            vaccine
            batch
            vaccination_time
            comments
            email
        }
    }
`;

function AddVaccination({ animalId }) {
    const [vaccines, setVaccines] = useState([]);
    const [vaccination, setVaccination] = useState({
        vaccine: 'Rabies',
        batch: 'RTL-0001',
        vaccination_time: new Date().toISOString().split('.')[0],
        comments: 'Add new vaccine',
        email: 'nolkoeva@gmail.com'
    });
    const [validationError, setError] = useState(null);
    const [addVaccination] = useMutation(ADD_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY]
    });

    const config = useConfig();
    if (!config) {
      return (
        <tr>
          <td colSpan="5">Loading animals configs...</td>
        </tr>
      );

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setVaccination({
            ...vaccination,
            [name]: value,
        });
    };

    const clearFields = () => {
        setVaccination({
            vaccine: 'Rabies',
            batch: 'RTL-0001',
            vaccination_time: new Date().toISOString().split('.')[0],
            comments: 'Add new vaccine',
            email: 'nolkoeva@gmail.com'
        });
    }

    return (
        <tr>
            <td></td>
            <td>
                <select name="vaccine" value={vaccination.vaccine} onChange={handleInputChange}>
                    {config.vaccines.map(vaccine => (
                        <option key={vaccine} value={vaccine}>{vaccine}</option>
                    ))}
                </select>
                {validationError === 'vaccine' && <span>Vaccine is mandatory</span>}
            </td>
            <td><input name="batch" value={vaccination.batch}
                onChange={handleInputChange}
                placeholder={validationError === 'batch' ? 'batch is mandatory' : ''} /></td>
            <td><input className="long" name="vaccination_time" value={vaccination.vaccination_time}
                onChange={handleInputChange}
                placeholder={validationError === 'vaccination_time' ? 'vaccination_time is mandatory' : ''} />
            </td>
            <td><input name="comments" value={vaccination.comments}
                onChange={handleInputChange} /></td>
            <td><input name="email" value={vaccination.email} onChange={handleInputChange} /></td>
            <td>
                <button className="button" onClick={() => {
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

                    addVaccination({
                        variables: {
                            animalId: animalId,
                            vaccine: vaccination.vaccine,
                            batch: vaccination.batch,
                            vaccination_time: vaccination.vaccination_time,
                            comments: vaccination.comments,
                            email: vaccination.email
                        }
                    }).catch(error => { showError({ error: error }) });

                    clearFields();
                }} className="round-button-with-border">
                    Add
                </button>
            </td>
        </tr>
    );
}

export default AddVaccination;
