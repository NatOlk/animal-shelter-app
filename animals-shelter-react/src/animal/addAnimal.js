import React, { useState, useEffect } from 'react';
import { useMutation } from '@apollo/client';
import showError from '../common/showError';
import { useConfig } from '../common/configContext';
import { Button, Input, Select, SelectSection, SelectItem, Spacer } from '@nextui-org/react';
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell} from "@nextui-org/react";
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
            <TableRow>
                <TableCell colSpan="5">Loading animals configs...</TableCell>
            </TableRow>
        );
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setAnimal({
            ...animal,
            [name]: value,
        });
    };

    const handleSelectChange = (name, value) => {
        setAnimal((prevAnimal) => ({
            ...prevAnimal,
            [name]: value,
        }));
    };

    useEffect(() => {
        const datepickerElems = document.querySelectorAll('.datepicker');
        const instances = M.Datepicker.init(datepickerElems, {
            format: 'yyyy-mm-dd',
            onSelect: (date) => {
                setAnimal(prevAnimal => ({
                    ...prevAnimal,
                    birthDate: date.toISOString().split('T')[0]
                }));
            },
        });
        return () => {
            instances.forEach((instance) => instance.destroy());
        };
    }, []);

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

        addAnimal({
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
         <TableRow>
            <TableCell></TableCell>
            <TableCell>
                <Input
                    name="name"
                    value={animal.name}
                    onChange={handleInputChange}
                    aria-label="Animal Name"
                    status={validationError === 'name' ? 'error' : 'default'}
                    helperText={validationError === 'name' ? 'Name is mandatory' : ''}
                />
            </TableCell>
            <TableCell>
                <Select
                    selectedKeys={new Set([animal.species])}
                    onValueChange={(value) => handleSelectChange('species', value)}
                    aria-label="Species"
                    validationState={validationError === 'species' ? 'invalid' : 'valid'}>
                    {config.config.animals.map(animal => (
                        <SelectItem key={animal} value={animal}>{animal}</SelectItem>
                    ))}
                </Select>
            </TableCell>
            <TableCell>
                <Select
                    selectedKeys={new Set([animal.primaryColor])}
                    onValueChange={(value) => handleSelectChange('primaryColor', value)}
                    aria-label="Primary Color"
                    validationState={validationError === 'primaryColor' ? 'invalid' : 'valid'}>
                    {config.config.colors.map(color => (
                        <SelectItem key={color} value={color}>{color}</SelectItem>
                    ))}
                </Select>
            </TableCell>
            <TableCell>
                <Input
                    name="breed"
                    value={animal.breed}
                    onChange={handleInputChange}
                    aria-label="Breed"/>
            </TableCell>
            <TableCell>
                <Input
                    name="implantChipId"
                    value={animal.implantChipId}
                    onChange={handleInputChange}
                    aria-label="Implant Chip ID"
                    status={validationError === 'implantChipId' ? 'error' : 'default'}
                    helperText={validationError === 'implantChipId' ? 'Implant Chip ID is mandatory' : ''}/>
            </TableCell>
            <TableCell>
                <Select
                    selectedKeys={new Set([animal.gender])}
                    onValueChange={(value) => handleSelectChange('gender', value)}
                    aria-label="Gender"
                    validationState={validationError === 'gender' ? 'invalid' : 'valid'}>
                    {config.config.genders.map(gender => (
                        <SelectItem key={gender} value={gender}>{gender}</SelectItem>
                    ))}
                </Select>
            </TableCell>
            <TableCell>
                <Input
                    name="birthDate"
                    value={animal.birthDate}
                    readOnly
                    aria-label="Birth Date"
                    className="datepicker"/>
            </TableCell>
            <TableCell>
                <Input
                    name="pattern"
                    value={animal.pattern}
                    onChange={handleInputChange}
                    aria-label="Pattern"/>
            </TableCell>
            <TableCell>
                <Button
                    onClick={handleAddAnimal}
                    color="success"
                    auto
                    icon={<i className="small material-icons">add</i>}>
                    Add Animal
                </Button>
            </TableCell>
        </TableRow>
    );
}

export default AddAnimal;
