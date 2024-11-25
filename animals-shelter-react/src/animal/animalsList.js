import React, { useState, useEffect } from "react";
import { Container, Table } from "reactstrap";
import { useQuery } from "@apollo/client";
import AddAnimal from "./addAnimal";
import UpdateAnimal from "./updateAnimal";
import { ANIMALS_QUERY } from "../common/graphqlQueries.js";
import Pagination from "../common/pagination";
import { useConfig } from "../common/configContext";

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

    const animalsList = filteredAnimals
        .slice(currentPage * perPage, (currentPage + 1) * perPage)
        .map((animal) => (
            <UpdateAnimal
                key={animal.id}
                animal={{
                    ...animal,
                    birthDate: formatDate(animal.birthDate),
                }}
            />
        ));

    return (
        <div>
            <div id="error" className="errorAlarm"></div>

            <Container fluid>
                <div class="row">
                    <div class="col s2">
                        <label htmlFor="speciesFilter">Filter by species:</label>
                        <select
                            id="speciesFilter"
                            value={selectedSpecies}
                            onChange={(e) => {
                                setSelectedSpecies(e.target.value);
                                setCurrentPage(0);
                            }}
                            className="browser-default mandatory">
                            <option value="all">All</option>
                            {config.config.animals.map((species) => (
                                <option key={species} value={species}>
                                    {species}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                <Table className="highlight responsive-table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th>Species</th>
                            <th>Primary color</th>
                            <th>Breed</th>
                            <th>Implant chip id</th>
                            <th>Gender</th>
                            <th>Birth date</th>
                            <th>Pattern</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <AddAnimal />
                        {animalsList}
                    </tbody>
                </Table>
                <Pagination
                    currentPage={currentPage}
                    pageCount={pageCount}
                    onPageChange={setCurrentPage}
                />
            </Container >
        </div >
    );
}

export default AnimalsList;
