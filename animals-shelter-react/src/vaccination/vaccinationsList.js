import React, { useState } from 'react';
import { Container, Table } from 'reactstrap';
import { useQuery } from "@apollo/client";
import AddVaccination from "./addVaccination";
import UpdateVaccination from "./updateVaccination";
import { useLocation } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { VACCINATIONS_QUERY } from '../common/graphqlQueries.js';

function VaccinationsList() {
    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);

    const location = useLocation();
    const { animalId, name, species } = location.state;

    if (!animalId) {
        return (
            <div>
                <Link to="/">Back to Animals</Link>
                <p>Error: No animalId provided!</p>
            </div>
        );
    }

    const { loading, error, data } = useQuery(VACCINATIONS_QUERY, {
        variables: { animalId: animalId },
        fetchPolicy: 'network-only',
    });

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error :(</p>;

    const pageCount = Math.ceil(data.vaccinationByAnimalId.length / perPage);

    const formatDate = (dateString) => {
        if (!dateString) return "";
        const date = new Date(dateString);
        return date.toISOString().slice(0, 10);
    };

    const vaccinationsList = data.vaccinationByAnimalId
        .slice(currentPage * perPage, (currentPage + 1) * perPage)
        .map(vaccination => (
            <UpdateVaccination
                key={vaccination.id}
                vaccination={{
                    ...vaccination,
                    vaccinationTime: formatDate(vaccination.vaccinationTime), // Форматируем дату
                }}
            />
        ));

    return (
        <div>
            <div id="error" className="errorAlarm"></div>
            <Link to="/">Back to Animals</Link>
            <Container fluid>
                <h5>Vaccinations for animal: {name} ({species})</h5>
                <Table className="highlight responsive-table">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Vaccine</th>
                            <th>Batch</th>
                            <th>Vaccination time</th>
                            <th>Comments</th>
                            <th>Email</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <AddVaccination animalId={animalId} />
                        {vaccinationsList}
                    </tbody>
                </Table>
                <div>
                    {
                        Array.from({ length: pageCount }, (_, index) => (
                            <button
                                key={index}
                                className="round-button-with-border"
                                onClick={() => setCurrentPage(index)}
                            >
                                {index + 1}
                            </button>
                        ))
                    }
                </div>
            </Container>
        </div>
    );
}

export default VaccinationsList;
