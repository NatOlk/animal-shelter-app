import React, { useState, useEffect } from "react";
import {
    Select, SelectSection, SelectItem, Spacer,
    Table, TableHeader, TableColumn, TableBody, TableRow, TableCell,
    Button, Input
} from "@nextui-org/react";
import { useQuery, useMutation } from "@apollo/client";
import DeleteAnimal from './deleteAnimal';
import EditableAnimalField from './editableAnimalField';
import { ANIMALS_QUERY, ADD_ANIMAL } from "../common/graphqlQueries.js";
import Pagination from "../common/pagination";
import { useConfig } from "../common/configContext";
import { Link } from 'react-router-dom';
import { DatePicker } from "@nextui-org/date-picker";
import { parseDate, getLocalTimeZone } from "@internationalized/date";
import { useDateFormatter } from "@react-aria/i18n";
import { IoIosAddCircleOutline } from "react-icons/io"
import { BiInjection } from "react-icons/bi";

function AnimalsList() {
    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);
    const [selectedSpecies, setSelectedSpecies] = useState("all");
    const [birthDate, setBirthDate] = React.useState(parseDate("2024-04-16"));

    const formatter = new Intl.DateTimeFormat("en-US", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
    });

    const { loading, error, data, refetch } = useQuery(ANIMALS_QUERY);
    const config = useConfig();

    const initialValues = {
        name: "",
        species: "Cat",
        primaryColor: "White",
        gender: "F",
        breed: "",
        implantChipId: "00000000-00000000-0000",
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
    useEffect(() => {
        refetch();
    }, [refetch]);

    if (config.config == null) return <p>Loading...</p>;
    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error :(</p>;

    const allAnimals = data.allAnimals;

    const filteredAnimals =
        selectedSpecies === "all"
            ? allAnimals
            : allAnimals.filter((animal) => animal.species === selectedSpecies);

    const pageCount = Math.ceil(filteredAnimals.length / perPage);

    const paginatedAnimals = filteredAnimals.slice(
        currentPage * perPage,
        (currentPage + 1) * perPage);

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

        addAnimal({
            variables: {
                name: animal.name,
                species: animal.species,
                primaryColor: animal.primaryColor,
                breed: animal.breed,
                implantChipId: animal.implantChipId,
                gender: animal.gender,
                birthDate: birthDate ? formatter.format(birthDate.toDate(getLocalTimeZone())) : "01/01/2025",
                pattern: animal.pattern
            }
        }).catch((error1) => {
            console.error("Error adding animal:", error1);
        });

        clearFields();
    };

    const clearFields = () => {
        setAnimal(initialValues);
    };

    return (
        <div>
            <div id="error" className="errorAlarm"></div>

            <div style={{ display: "flex", gap: "1rem", marginBottom: "1rem" }}>
                <label htmlFor="speciesFilter" style={{ alignSelf: "center" }}>
                    Filter by species:
                </label>
                <Select
                    id="speciesFilter"
                    value={selectedSpecies}
                    onValueChange={(value) => {
                        setSelectedSpecies(value);
                        setCurrentPage(0);
                    }}
                    aria-label="Filter by species">
                    <SelectSection title="Species">
                        <SelectItem key="all" value="all">
                            All
                        </SelectItem>
                        {config.config.animals.map(species => (
                            <SelectItem key={species}>
                                {species}
                            </SelectItem>
                        ))}
                    </SelectSection>
                </Select>
            </div>
            <Table className="compact-table">
                <TableHeader>
                    <TableColumn>#</TableColumn>
                    <TableColumn>Name</TableColumn>
                    <TableColumn>Species</TableColumn>
                    <TableColumn>Primary color</TableColumn>
                    <TableColumn>Breed</TableColumn>
                    <TableColumn>Implant chip id</TableColumn>
                    <TableColumn>Gender</TableColumn>
                    <TableColumn>Birth date</TableColumn>
                    <TableColumn>Pattern</TableColumn>
                    <TableColumn>Actions</TableColumn>
                </TableHeader>
                <TableBody>
                    <TableRow key="0" className="highlighted-row">
                        <TableCell></TableCell>
                        <TableCell>
                            <Input
                                name="name" value={animal.name}
                                className="w-full md:w-32"
                                onChange={handleInputChange}
                                isRequired />
                        </TableCell>
                        <TableCell>
                            <Select
                                name="species"
                                defaultSelectedKeys={["Cat"]}
                                className="w-full md:w-28"
                                variant="bordered"
                                isRequired
                                onChange={handleInputChange}>
                                {config.config.animals.map(animal => (
                                    <SelectItem key={animal}>{animal}</SelectItem>
                                ))}
                            </Select>
                        </TableCell>
                        <TableCell>
                            <Select
                                name="primaryColor"
                                className="w-full md:w-28"
                                defaultSelectedKeys={["White"]}
                                isRequired
                                onChange={handleInputChange}>
                                {config.config.colors.map(color => (
                                    <SelectItem key={color}>{color}</SelectItem>
                                ))}
                            </Select>
                        </TableCell>
                        <TableCell>
                            <Input
                                name="breed" value={animal.breed}
                                onChange={handleInputChange}
                                className="w-full md:w-24" />
                        </TableCell>
                        <TableCell>
                            <Input
                                name="implantChipId" value={animal.implantChipId}
                                isRequired
                                onChange={handleInputChange} />
                        </TableCell>
                        <TableCell>
                            <Select
                                name="gender"
                                defaultSelectedKeys={["F"]}
                                className="w-full md:w-16"
                                isRequired
                                onChange={handleInputChange}>
                                {config.config.genders.map(gender => (
                                    <SelectItem key={gender}>{gender}</SelectItem>
                                ))}
                            </Select>
                        </TableCell>
                        <TableCell>
                            <div className="flex w-full flex-wrap md:flex-nowrap gap-4">
                                <DatePicker isRequired className="max-w-[284px]"
                                    name="birthDate" value={birthDate}
                                    onChange={setBirthDate} />
                            </div>
                        </TableCell>
                        <TableCell>
                            <Input
                                name="pattern" value={animal.pattern}
                                className="w-full md:w-24"
                                onChange={handleInputChange} />
                        </TableCell>
                        <TableCell>
                            <Button
                                onPress={handleAddAnimal}
                                color="default" variant="light"
                                className="p-2 min-w-2 h-auto">
                                <IoIosAddCircleOutline />
                            </Button>
                        </TableCell>
                    </TableRow>
                    {paginatedAnimals.map((animal, index) => (
                        <TableRow key={animal.id} className="table-row">
                            <TableCell>{animal.id}</TableCell>
                            <TableCell>{animal.name}</TableCell>
                            <TableCell>{animal.species}</TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal} value={animal.primaryColor}
                                    name="primaryColor"
                                    values={config.config.colors} />
                            </TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal} value={animal.breed}
                                    name="breed" />
                            </TableCell>
                            <TableCell>{animal.implantChipId}</TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal} value={animal.gender}
                                    name="gender"
                                    values={config.config.genders} />
                            </TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal} value={animal.birthDate}
                                    name="birthDate"
                                    isDate={true} />
                            </TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal} value={animal.pattern}
                                    name="pattern" />
                            </TableCell>
                            <TableCell>
                                <div className="flex w-full flex-wrap flex-nowrap">
                                    <Button as={Link} to={`/vaccinations`}
                                        state={{ animalId: animal.id, name: animal.name, species: animal.species }}
                                        color="default" variant="light"
                                        className="p-2 min-w-2 h-auto">
                                        {animal.vaccinationCount}
                                        <BiInjection />
                                    </Button>
                                    &nbsp;
                                    <DeleteAnimal id={animal.id} />
                                </div>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <Spacer y={1} />
            <Pagination
                currentPage={currentPage}
                pageCount={pageCount}
                onPageChange={setCurrentPage}
            />
        </div>
    );
};

export default AnimalsList;
