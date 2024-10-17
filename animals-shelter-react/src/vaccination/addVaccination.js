import React, { useState, useEffect } from 'react';
import { useMutation } from "@apollo/client";
import axios from 'axios';
import showError from "./showError";
import { useConfig } from '../common/configContext';
import { VACCINATIONS_QUERY, ADD_VACCINATION } from '../common/graphqlQueries.js';

function AddVaccination({ animalId }) {
    const [vaccines, setVaccines] = useState([]);
    const [vaccination, setVaccination] = useState({
        vaccine: 'Rabies',
        batch: 'RTL-0001',
        vaccinationTime: new Date().toISOString().split('.')[0],
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
    };
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
            vaccinationTime: new Date().toISOString().split('.')[0],
            comments: 'Add new vaccine',
            email: 'nolkoeva@gmail.com'
        });
    }

    return (
        <tr>
            <td></td>
            <td>
                <select name="vaccine"
                value={vaccination.vaccine}
                className="browser-default"
                onChange={handleInputChange}>
                    {config.config.vaccines.map(vaccine => (
                        <option key={vaccine} value={vaccine}>{vaccine}</option>
                    ))}
                </select>
                {validationError === 'vaccine' && <span>Vaccine is mandatory</span>}
            </td>
            <td><input name="batch" value={vaccination.batch}
                onChange={handleInputChange}
                placeholder={validationError === 'batch' ? 'batch is mandatory' : ''} /></td>
            <td><input className="long" name="vaccinationTime" value={vaccination.vaccinationTime}
                onChange={handleInputChange}
                placeholder={validationError === 'vaccinationTime' ? 'vaccinationTime is mandatory' : ''} />
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
                    if (!vaccination.vaccinationTime) {
                        setError('vaccinationTime');
                        return;
                    }

                    addVaccination({
                        variables: {
                            animalId: animalId,
                            vaccine: vaccination.vaccine,
                            batch: vaccination.batch,
                            vaccinationTime: vaccination.vaccinationTime,
                            comments: vaccination.comments,
                            email: vaccination.email
                        }
                    }).catch(error => { showError({ error: error }) });

                    clearFields();
                }} className="waves-effect waves-light btn-small">
                    Add
                </button>
            </td>
        </tr>
    );
}

export default AddVaccination;
