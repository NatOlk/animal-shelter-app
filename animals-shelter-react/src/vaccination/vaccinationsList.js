import React, { useState } from 'react';
import { useQuery } from "@apollo/client";
import AddVaccination from "./addVaccination";
import UpdateVaccination from "./updateVaccination";
import DeleteVaccination from "./deleteVaccination";
import { useLocation, Link } from 'react-router-dom';
import { VACCINATIONS_QUERY } from '../common/graphqlQueries.js';
import Pagination from '../common/pagination'
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
        .slice(currentPage * perPage, (currentPage + 1) * perPage);

    return (
        <div>
            <div id="error" className="errorAlarm"></div>
            <Link to="/">Back to Animals</Link>
            <Table className="highlight responsive-table">
                <TableHeader>
                    <TableColumn>#</TableColumn>
                    <TableColumn>Vaccine</TableColumn>
                    <TableColumn>Batch</TableColumn>
                    <TableColumn>Vaccination time</TableColumn>
                    <TableColumn>Comments</TableColumn>
                    <TableColumn>Email</TableColumn>
                    <TableColumn>Actions</TableColumn>
                </TableHeader>
                <TableBody>
                    {vaccinationsList.map((vaccination, index) => (
                        <TableRow key={vaccination.id}>
                            <TableCell>{vaccination.id}</TableCell>
                            <TableCell>{vaccination.vaccine}</TableCell>
                            <TableCell>{vaccination.batch}</TableCell>
                            <TableCell>{formatDate(vaccination.vaccinationTime)}</TableCell>
                            <TableCell>{vaccination.comments}</TableCell>
                            <TableCell>{vaccination.email}</TableCell>
                            <TableCell>
                                <DeleteVaccination id={vaccination.id} />
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <Pagination
                currentPage={currentPage}
                pageCount={pageCount}
                onPageChange={setCurrentPage} />
        </div>
    );
}

export default VaccinationsList;
