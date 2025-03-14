import React, { useState, useEffect, useRef } from 'react';
import { useQuery } from "@apollo/client";
import { useAsyncList } from "@react-stately/data";
import {
    Table, TableHeader, TableColumn, TableBody,
    Pagination, Progress, Alert
} from "@nextui-org/react";
import { useLocation, Link } from 'react-router-dom';
import { VACCINATIONS_QUERY } from '../common/graphqlQueries';
import VaccinationRow from './vaccinationRow';
import AddVaccination from './addVaccination';
import { useConfig } from '../common/configContext';
import { Vaccination, Config } from "../common/types";

const VaccinationsList: React.FC = () => {

    const perPage = 8;
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [globalError, setGlobalError] = useState<string>("");
    const location = useLocation();
    const animalIdRef = useRef<string | null>(location.state?.animalId || null);

    const config: Config | null = useConfig();

    const { loading, error, data } = useQuery<{ vaccinationByAnimalId: Vaccination[] }>(
        VACCINATIONS_QUERY, {
        variables: { animalId: animalIdRef.current  },
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

    if (!animalIdRef.current) {
        return (
            <div>
                <Link to="/">Back to Animals</Link>
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
                 <Link to="/">Back to Animals</Link>
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
                            AddVaccination({ config, animalId: animalIdRef.current, onError: handleError }),
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
