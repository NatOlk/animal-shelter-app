import React, { useState, useEffect } from "react";
import {
    Select,
    SelectSection,
    SelectItem,
    Spacer,
    Table,
    TableHeader,
    TableColumn,
    TableBody,
    TableRow,
    TableCell,
} from "@nextui-org/react";
import { useQuery } from "@apollo/client";
import AddAnimal from "./addAnimal";
import DeleteAnimal from './deleteAnimal';
import EditableAnimalField from './editableAnimalField';
import { ANIMALS_QUERY } from "../common/graphqlQueries.js";
import Pagination from "../common/pagination";
import { useConfig } from "../common/configContext";
import { Button } from "@nextui-org/react";
import { Link } from 'react-router-dom';

function AnimalsList() {
    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);
    const [selectedSpecies, setSelectedSpecies] = useState("all");
    const { loading, error, data, refetch } = useQuery(ANIMALS_QUERY);
    const config = useConfig();

    useEffect(() => {
        refetch();
    }, [refetch]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error :(</p>;

    const allAnimals = data.allAnimals;

    const filteredAnimals =
        selectedSpecies === "all"
            ? allAnimals
            : allAnimals.filter((animal) => animal.species === selectedSpecies);

    const pageCount = Math.ceil(filteredAnimals.length / perPage);

    const formatDate = (dateString) => {
        if (!dateString) return "";
        const date = new Date(dateString);
        return date.toISOString().slice(0, 10);
    };

    const paginatedAnimals = filteredAnimals.slice(
        currentPage * perPage,
        (currentPage + 1) * perPage);

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
                        {config.config.animals.map((species) => (
                            <SelectItem key={species} value={species}>
                                {species}
                            </SelectItem>
                        ))}
                    </SelectSection>
                </Select>
            </div>

            <Table removeWrapper isStriped aria-label="Animals list">
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
                    {paginatedAnimals.map((animal, index) => (
                        <TableRow key={animal.id}>
                            <TableCell>{animal.id}</TableCell>
                            <TableCell>{animal.name}</TableCell>
                            <TableCell>{animal.species}</TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal}
                                    value={animal.primaryColor}
                                    name="primaryColor"
                                    values={config.config.colors} />
                            </TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal}
                                    value={animal.breed}
                                    name="breed" />
                            </TableCell>
                            <TableCell>{animal.implantChipId}</TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal}
                                    value={animal.gender}
                                    name="gender"
                                    values={config.config.genders} />
                            </TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal}
                                    value={animal.birthDate}
                                    name="birthDate"
                                    isDate={true} />
                            </TableCell>
                            <TableCell>
                                <EditableAnimalField
                                    animal={animal}
                                    value={animal.pattern}
                                    name="pattern" />
                            </TableCell>
                            <TableCell>
                                <DeleteAnimal id={animal.id} />
                                <Link
                                    className="waves-effect waves-orange btn-small"
                                    to={`/vaccinations`}
                                    state={{ animalId: animal.id, name: animal.name, species: animal.species }}>
                                    <i className="small material-icons">vaccines</i>
                                    <span className="font12"> {animal.vaccinationCount}</span>
                                </Link>
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
}

export default AnimalsList;
