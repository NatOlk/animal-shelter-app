import React, { useState } from 'react';
import { Container, Table } from 'reactstrap';
import { gql } from 'graphql-tag';
import { useQuery } from "@apollo/client";
import DeleteVaccination from "./deleteVaccination";

const ALL_VACCINATIONS_QUERY = gql`
    {
        allVaccinations {
            id
            vaccine
            batch
            vaccinationTime
            comments
            email
            animal {
                id
                name
                species
            }
        }
    }
`;

function AllVaccinationsList() {
    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);

    const { loading, error, data } = useQuery(ALL_VACCINATIONS_QUERY, {
        fetchPolicy: "network-only",
    });

    if (loading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p>Error: {error.message}</p>;
    }


    const pageCount = Math.ceil(data.allVaccinations.length / perPage);
    const vaccinationsList = data.allVaccinations
        .slice(currentPage * perPage, (currentPage + 1) * perPage)
        .map(vaccination => (
            <tr key={vaccination.id}>
                <td>{vaccination.animal.name}</td>
                <td>{vaccination.animal.species}</td>
                <td>{vaccination.id}</td>
                <td>{vaccination.vaccine}</td>
                <td>{vaccination.batch}</td>
                <td>{vaccination.vaccinationTime}</td>
                <td>{vaccination.comments}</td>
                <td>{vaccination.email}</td>
                <td>
                    <DeleteVaccination id={vaccination.id}/>
                </td>
            </tr>
        ));

    return (
        <div>
            <div id="error" className="errorAlarm"></div>
            <Container fluid>
                <Table className="highlight responsive-table">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Species</th>
                            <th>Vaccine</th>
                            <th>Batch</th>
                            <th>Vaccination time</th>
                            <th>Comments</th>
                            <th>Email</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {vaccinationsList}
                    </tbody>
                </Table>
                <div>
                    {
                        Array.from({ length: pageCount }, (_, index) => (
                            <button
                                key={index}
                                className="round-button-with-border"
                                onClick={() => setCurrentPage(index)}>
                                {index + 1}
                            </button>
                        ))
                    }
                </div>
            </Container>
        </div>
    );
}

export default AllVaccinationsList;
