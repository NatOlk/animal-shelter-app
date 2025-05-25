import React, { useState, useEffect } from 'react';
import { useQuery } from "@apollo/client";
import { useAsyncList } from "@react-stately/data";
import {
    Table, TableHeader, TableColumn, TableBody,
    Pagination, Progress, Alert, Spacer
} from "@nextui-org/react";
import { useParams } from "react-router-dom";
import { VACCINATIONS_QUERY } from '../common/graphqlQueries';
import VaccinationRow from './vaccinationRow';
import AddVaccination from './addVaccination';
import { useConfig } from '../common/configContext';
import { Vaccination, Config } from "../common/types";

const VaccinationsList: React.FC<{ animalId?: string }> = ({ animalId }) => {
    const perPage = 8;
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [globalError, setGlobalError] = useState<string>("");
    const { animalId: animalIdParam } = useParams<{ animalId?: string }>();
    const animalIdReq = animalId || animalIdParam;

    const config: Config | null = useConfig();

    const { loading, error, data } = useQuery<{ vaccinationByAnimalId: Vaccination[] }>(
        VACCINATIONS_QUERY, {
        variables: { animalId: animalIdReq },
        fetchPolicy: "network-only"
    }
    );

    const list = useAsyncList<Vaccination>({
        async load() {
            if (loading) {
                return { items: [] };
            }
            if (error) {
                return { items: [] };
            }
            return {
                items: data?.vaccinationByAnimalId || [],
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

    if (config == null) return <p>Loading configs...</p>;

    if (!animalIdReq) {
        return (
            <div>
                <p>Error: No animalId provided!</p>
            </div>
        );
    }

    const handleError = (error) => {
        setGlobalError(error);
    };

    const vaccinationsList = list.items
        .slice(currentPage * perPage, (currentPage + 1) * perPage);

    const pageCount = Math.ceil(list.items.length / perPage);

    return (
        <>
            <div>
                {loading && (
                    <div>
                        <p> Loading...</p>
                        <Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />
                    </div>
                )}
                {error && <p>Error :(</p>}
            </div>
            <div>
                {globalError && (
                    <Alert
                        dismissable
                        color="danger"
                        variant="bordered"
                        onClose={() => setGlobalError("")}
                        title={globalError} />
                )}
                <Spacer y={5} />
                <Table className="compact-table"
                    isLoading={list.isLoading}
                    sortDescriptor={list.sortDescriptor}
                    onSortChange={list.sort}>
                    <TableHeader>
                        <TableColumn>#</TableColumn>
                        <TableColumn key="vaccine" allowsSorting>Vaccine</TableColumn>
                        <TableColumn key="batch" allowsSorting>Batch</TableColumn>
                        <TableColumn key="vaccinationTime" allowsSorting>Vaccination time</TableColumn>
                        <TableColumn>Comments</TableColumn>
                        <TableColumn key="email" allowsSorting>Email</TableColumn>
                        <TableColumn>Actions</TableColumn>
                    </TableHeader>
                    <TableBody>
                        {[
                            AddVaccination({ config, animalId: animalIdReq, onError: handleError }),
                            ...vaccinationsList.map((vaccination) => VaccinationRow({ vaccination, onError: handleError }))
                        ]}
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
        </>
    );
};

export default VaccinationsList;
