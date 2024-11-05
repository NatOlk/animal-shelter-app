import React, { useState, useEffect } from 'react';
import { useMutation } from '@apollo/client';
import showError from '../common/showError';
import { useConfig } from '../common/configContext';
import M from 'materialize-css';
import { ADD_ANIMAL, ANIMALS_QUERY } from '../common/graphqlQueries.js';

function AddAnimal() {
    const initialValues = {
        name: "",
        species: "Dog",
        primaryColor: "White",
        gender: "F",
        breed: "",
        implantChipId: "11111111-1111-1111-1111-111111111111",
        birthDate: "2012-01-01",
        pattern: "Broken"
    };
    useEffect(() => {
        const datepickerElems = document.querySelectorAll('.datepicker');
        const instances = M.Datepicker.init(datepickerElems, {
            format: 'yyyy-mm-dd',
            onSelect: (date) => {
                handleInputChange({ target: { name: 'birthDate', value: date } });
            },
        });
        return () => {
            instances.forEach((instance) => {
                instance.destroy();
            });
        };
    }, []);

    const [animal, setAnimal] = useState(initialValues);
    const [validationError, setValidationError] = useState(null);
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
        setAnimal({
            ...animal,
            [name]: value,
        });
    };
    const handleAddAnimal = () => {
        if (!animal.name) {
            setValidationError('name');
            return;
        }
        if (!animal.species) {
            setValidationError('species');
            return;
        }
        if (!animal.implantChipId) {
            setValidationError('implantChipId');
            return;
        }
        if (!animal.primaryColor) {
            setValidationError('primaryColor');
            return;
        }
        if (!animal.gender) {
            setValidationError('gender');
            return;
        }

        addAnimal(
            {
                variables: {
                    name: animal.name,
                    species: animal.species,
                    primaryColor: animal.primaryColor,
                    breed: animal.breed,
                    implantChipId: animal.implantChipId,
                    gender: animal.gender,
                    birthDate: animal.birthDate,
                    pattern: animal.pattern
                }
            }).catch((error1) => { showError({ error: error1 }) });

        clearFields();
    };
    const clearFields = () => {
        setAnimal(initialValues);
    };

    return (
        <tr>
            <td></td>
            <td><input name="name"
                value={animal.name}
                onChange={handleInputChange}
                className="table-column-breed mandatory"
                placeholder={validationError === 'name' ? 'Name is mandatory' : ''} />
            </td>
            <td>
                <select
                    name="species"
                    value={animal.species}
                    onChange={handleInputChange}
                    className="browser-default mandatory"
                    placeholder={validationError === 'species' ? 'Species is mandatory' : ''}>
                    {config.config.animals.map(animal => (
                        <option key={animal} value={animal}>{animal}</option>
                    ))}
                </select>
            </td>
            <td>
                <select
                    name="primaryColor"
                    value={animal.primaryColor}
                    onChange={handleInputChange}
                    className="browser-default mandatory"
                    placeholder={validationError === 'primaryColor' ? 'Primary color is mandatory' : ''}>
                    {config.config.colors.map(color => (
                        <option key={color} value={color}>{color}</option>
                    ))}
                </select>
            </td>
            <td>
                <input name="breed"
                    value={animal.breed}
                    onChange={handleInputChange}
                    className='table-column-breed' />
            </td>
            <td>
                <input
                    name="implantChipId"
                    value={animal.implantChipId}
                    onChange={handleInputChange}
                    className="table-column-chip mandatory"
                    placeholder={validationError === 'implantChipId' ? 'implantChipId is mandatory' : ''} />
            </td>
            <td>
                <select
                    name="gender"
                    value={animal.gender}
                    onChange={handleInputChange}
                    className="browser-default table-column-gender mandatory"
                    placeholder={validationError === 'gender' ? 'Gender is mandatory' : ''}>
                    {config.config.genders.map(gender => (
                        <option key={gender} value={gender}>{gender}</option>
                    ))}
                </select>
            </td>
            <td>
                <input name="birthDate"
                    className="datepicker"
                    value={animal.birthDate}
                    onChange={handleInputChange} />
            </td>
            <td>
                <input name="pattern"
                    value={animal.pattern}
                    onChange={handleInputChange}
                    className='table-column-breed' />
            </td>
            <td>
                <button onClick={handleAddAnimal} className="waves-effect waves-orange btn-small">
                    <i className="small material-icons">add</i>
                </button>
            </td>
        </tr>
    );
}

export default AddAnimal;
