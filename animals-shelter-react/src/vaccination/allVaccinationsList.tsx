import React, { useState, useEffect } from 'react';
import { useQuery } from "@apollo/client";
import DeleteVaccination from "./deleteVaccination";
import { ALL_VACCINATIONS_QUERY } from '../common/graphqlQueries';
import {
    Pagination, Progress, Alert, Table, TableHeader,
    TableColumn, TableBody, TableRow, TableCell
} from "@nextui-org/react";
import { useAsyncList } from "@react-stately/data";
import { Vaccination } from "../common/types";

const AllVaccinationsList: React.FC = () => {
    const perPage = 10;
    const [currentPage, setCurrentPage] = useState<number>(0);
    const { loading, error, data } = useQuery<{ allVaccinations: Vaccination[] }>(ALL_VACCINATIONS_QUERY, {
        fetchPolicy: 'network-only',
    });
    const [globalError, setGlobalError] = useState<string>("");

    const list = useAsyncList<Vaccination>({
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
                    let first = a[sortDescriptor.column as keyof Vaccination];
                    let second = b[sortDescriptor.column as keyof Vaccination];
                    let cmp = (parseInt(first as string) || first) < (parseInt(second as string) || second) ? -1 : 1;

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

    const handleError = (error: string) => {
        setGlobalError(error);
    };

    const pageCount = Math.ceil(list.items.length / perPage);
    const vaccinationsList = list.items
        .slice(currentPage * perPage, (currentPage + 1) * perPage);

    return (
        <div>
            <div className="flex flex-col gap-4 w-full">
                {globalError && (
                    <Alert
                        dismissable
                        color="danger"
                        variant="bordered"
                        onClose={() => setGlobalError("")}
                        title={globalError} />
                )}
            </div>
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
                            <TableCell>{vaccination.vaccinationTime}</TableCell>
                            <TableCell>{vaccination.comments}</TableCell>
                            <TableCell>{vaccination.email}</TableCell>
                            <TableCell>
                                <DeleteVaccination id={vaccination.id} onError={handleError} />
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
