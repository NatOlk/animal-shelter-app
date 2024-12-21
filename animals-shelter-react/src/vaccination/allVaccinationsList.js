import React, { useState } from 'react';
import { useQuery } from "@apollo/client";
import DeleteVaccination from "./deleteVaccination";
import { ALL_VACCINATIONS_QUERY } from '../common/graphqlQueries.js';
import Pagination from '../common/pagination'
import {
    Select, SelectSection, SelectItem, Spacer,
    Table, TableHeader, TableColumn, TableBody, TableRow, TableCell
} from "@nextui-org/react";
import { Link, Button } from "@nextui-org/react";

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
        .slice(currentPage * perPage, (currentPage + 1) * perPage);

    return (
        <div>
            <div id="error" className="errorAlarm"></div>
            <Table className="highlight responsive-table">
                <TableHeader>
                    <TableColumn>#</TableColumn>
                    <TableColumn>Name</TableColumn>
                    <TableColumn>Species</TableColumn>
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
                            <TableCell>{vaccination.animal.name}</TableCell>
                            <TableCell>{vaccination.animal.species}</TableCell>
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

export default AllVaccinationsList;
