import React, { useState, useEffect } from 'react';
import { useMutation } from "@apollo/client";
import showError from "./showError";
import { useConfig } from '../common/configContext';
import { VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY, ADD_VACCINATION } from '../common/graphqlQueries.js';
import M from 'materialize-css';

function AddVaccination({ animalId }) {
    const [vaccination, setVaccination] = useState({
        vaccine: 'Rabies',
        batch: '',
        vaccinationTime: new Date().toISOString().split('T')[0],
        comments: 'Add new vaccine',
        email: ''
    });
    useEffect(() => {
        var datepickerElems = document.querySelectorAll('.datepicker');
        M.Datepicker.init(datepickerElems, {
            format: 'yyyy-mm-dd',
            onSelect: (date) => {
                const selectedDate = date.toISOString().split('T')[0];
                setVaccination(prev => ({ ...prev, vaccinationTime: selectedDate }));
                // handleInputChange({ target: { name: 'vaccinationTime', value: date } });
            },
        });
    }, []);

    const [validationError, setError] = useState(null);
    const [addVaccination] = useMutation(ADD_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY],
        update(cache, { data: { addVaccination } }) {
            try {
                const { allVaccinations } = cache.readQuery({ query: ALL_VACCINATIONS_QUERY });
                cache.writeQuery({
                    query: ALL_VACCINATIONS_QUERY,
                    data: { allVaccinations: [...allVaccinations, addVaccination] },
                });
            } catch (error) {
                console.error("Error updating cache:", error);
            }
        }
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
            <td>
                <input name="batch"
                    value={vaccination.batch}
                    onChange={handleInputChange}
                    placeholder={validationError === 'batch' ? 'batch is mandatory' : ''} />
            </td>
            <td>
                <input
                    className="datepicker"
                    name="vaccinationTime"
                    value={vaccination.vaccinationTime}
                    onChange={handleInputChange}
                    placeholder={validationError === 'vaccinationTime' ? 'vaccinationTime is mandatory' : ''} />
            </td>
            <td>
                <input name="comments" value={vaccination.comments}
                    onChange={handleInputChange} />
            </td>
            <td>
                <input name="email" value={vaccination.email} onChange={handleInputChange} />
            </td>
            <td>
                <button className="waves-effect waves-light btn-small" onClick={() => {
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
                            vaccinationTime: vaccination.vaccinationTime.split('T')[0],
                            comments: vaccination.comments,
                            email: vaccination.email
                        }
                    }).catch(error => { showError({ error: error }) });

                    clearFields();
                }} >
                  <i className="small material-icons">add</i>
                </button>
            </td>
        </tr>
    );
}

export default AddVaccination;
