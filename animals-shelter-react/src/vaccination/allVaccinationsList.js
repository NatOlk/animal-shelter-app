import React, { useState, useEffect } from 'react';
import { useQuery } from "@apollo/client";
import DeleteVaccination from "./deleteVaccination";
import { ALL_VACCINATIONS_QUERY } from '../common/graphqlQueries.js';
import {
    Pagination, Progress, Spinner, Table, TableHeader,
    TableColumn, TableBody, TableRow, TableCell
} from "@nextui-org/react";
import { useAsyncList } from "@react-stately/data";

function AllVaccinationsList() {
    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);
    const { loading, error, data } = useQuery(ALL_VACCINATIONS_QUERY);

    const list = useAsyncList({
        async load() {
            if (loading) {
                return { items: [] };
            }
            if (error) {
                console.error("GraphQL Error:", error.message);
                return { items: [] };
            }
            return {
                items: data?.allVaccinations || [],
            };
        },
        async sort({ items, sortDescriptor }) {
            return {
                items: items.sort((a, b) => {
                    let first = a[sortDescriptor.column];
                    let second = b[sortDescriptor.column];
                    let cmp = (parseInt(first) || first) < (parseInt(second) || second) ? -1 : 1;

                    if (sortDescriptor.direction === "descending") {
                        cmp *= -1;
                    }
                    return cmp;
                }),
            };
        },
    });

    useEffect(() => {
        if (!loading && !error) {

            list.reload();
        }
    }, [loading, error, data]);

    if (loading) return (
        <div>
            <p> Loading...</p>
            <Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />
        </div>
    );

    if (error) {
        return <p>Error: {error.message}</p>;
    }

    const formatDate = (dateString) => {
        if (!dateString) return "";
        const date = new Date(dateString);
        return date.toISOString().slice(0, 10);
    };

    const pageCount = Math.ceil(list.items.length / perPage);
    const vaccinationsList = list.items
        .slice(currentPage * perPage, (currentPage + 1) * perPage);

    return (
        <div>
            <div id="error" className="errorAlarm"></div>
            <Table className="highlight responsive-table"
                isLoading={list.isLoading}
                sortDescriptor={list.sortDescriptor}
                onSortChange={list.sort}>
                <TableHeader>
                    <TableColumn>#</TableColumn>
                    <TableColumn>Name</TableColumn>
                    <TableColumn>Species</TableColumn>
                    <TableColumn key="vaccine" allowsSorting>Vaccine</TableColumn>
                    <TableColumn key="batch" allowsSorting>Batch</TableColumn>
                    <TableColumn key="vaccinationTime" allowsSorting>Vaccination time</TableColumn>
                    <TableColumn>Comments</TableColumn>
                    <TableColumn key="email" allowsSorting>Email</TableColumn>
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
                total={pageCount}
                page={currentPage + 1}
                onChange={(page) => setCurrentPage(page - 1)}
                showControls
                loop
                size="md"
                showShadow />
        </div>
    );
}

export default AllVaccinationsList;
