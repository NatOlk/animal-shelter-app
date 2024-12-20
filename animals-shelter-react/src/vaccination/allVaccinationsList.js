import React, { useState } from 'react';
import { Container, Table } from 'reactstrap';
import { useQuery } from "@apollo/client";
import DeleteVaccination from "./deleteVaccination";
import { ALL_VACCINATIONS_QUERY } from '../common/graphqlQueries.js';
import Pagination from '../common/pagination'

function AllVaccinationsList() {
    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);

    const { loading, error, data } = useQuery(ALL_VACCINATIONS_QUERY);

    if (loading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p>Error: {error.message}</p>;
    }

    const pageCount = Math.ceil(data.allVaccinations.length / perPage);

    const formatDate = (dateString) => {
        if (!dateString) return "";
        const date = new Date(dateString);
        return date.toISOString().slice(0, 10);
    };

    const vaccinationsList = data.allVaccinations
        .slice(currentPage * perPage, (currentPage + 1) * perPage)
        .map(vaccination => (
            <tr key={vaccination.id}>
                <td>{vaccination.id}</td>
                <td>{vaccination.animal.name}</td>
                <td>{vaccination.animal.species}</td>
                <td>{vaccination.vaccine}</td>
                <td>{vaccination.batch}</td>
                <td>{formatDate(vaccination.vaccinationTime)}</td>
                <td>{vaccination.comments}</td>
                <td>{vaccination.email}</td>
                <td>
                    <DeleteVaccination id={vaccination.id} />
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
                            <th>#</th>
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
                <Pagination
                    currentPage={currentPage}
                    pageCount={pageCount}
                    onPageChange={setCurrentPage}
                />
            </Container>
        </div>
    );
}

export default AllVaccinationsList;
