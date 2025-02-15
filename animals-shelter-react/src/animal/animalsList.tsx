import React, { useState, useEffect, useCallback } from "react";
import { useQuery } from "@apollo/client";
import { useAsyncList } from "@react-stately/data";
import {
    Table, TableHeader, TableColumn,
    TableBody, Pagination, Progress, Alert, Spacer
} from "@nextui-org/react";
import { useConfig } from "../common/configContext";
import AnimalRow from './animalRow';
import AddAnimal from './addAnimal';
import { ANIMALS_QUERY } from "../common/graphqlQueries";
import { Animal, Config } from "../common/types";

const AnimalsList: React.FC = () => {
    const perPage = 8;
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [globalError, setGlobalError] = useState<string>("");
    const { loading, error, data } = useQuery<{ allAnimals: Animal[] }>(ANIMALS_QUERY);
    const config: Config | null = useConfig();

    const list = useAsyncList<Animal>({
        async load() {
            if (loading) {
                return { items: [] };
            }

            if (error) {
                return { items: [] };
            }

            return {
                items: data?.allAnimals || [],
            };
        },
        async sort({ items, sortDescriptor }) {
            return {
                items: items.sort((a, b) => {
                    let first = a[sortDescriptor.column as keyof Animal];
                    let second = b[sortDescriptor.column as keyof Animal];
                    let cmp = (parseInt(first as string) || first) < (parseInt(second as string) || second) ? -1 : 1;
                    return sortDescriptor.direction === "descending" ? -cmp : cmp;
                }),
            };
        },
    });

    useEffect(() => {
        if (!loading && !error) {
            list.reload();
        }
    }, [loading, error, data]);

    const handleError = useCallback((error: string) => {
        setGlobalError(error);
    }, []);

    const pageCount = Math.ceil(list.items.length / perPage);
    const paginatedAnimals = list.items.slice(
        currentPage * perPage,
        (currentPage + 1) * perPage
    );

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
                <Table className="compact-table" isLoading={list.isLoading} sortDescriptor={list.sortDescriptor} onSortChange={list.sort}>
                    <TableHeader>
                        <TableColumn>#</TableColumn>
                        <TableColumn key="name" allowsSorting>Name</TableColumn>
                        <TableColumn key="species" allowsSorting>Species</TableColumn>
                        <TableColumn key="primaryColor" allowsSorting>Primary color</TableColumn>
                        <TableColumn key="bread" allowsSorting>Breed</TableColumn>
                        <TableColumn>Implant chip id</TableColumn>
                        <TableColumn key="gender" allowsSorting>Gender</TableColumn>
                        <TableColumn key="birthDate">Birth date</TableColumn>
                        <TableColumn key="pattern" allowsSorting>Pattern</TableColumn>
                        <TableColumn>Actions</TableColumn>
                    </TableHeader>
                    <TableBody>
                        {[
                            AddAnimal({ config, onError: handleError }),
                            ...paginatedAnimals.map((animal) => AnimalRow({ animal, config, onError: handleError }))
                        ]}
                    </TableBody>
                </Table>
                <Spacer y={1} />
                <Pagination
                    total={pageCount}
                    page={currentPage + 1}
                    onChange={(page) => setCurrentPage(page - 1)}
                    showControls loop size="md" showShadow />
            </div>
        </>
    );
};

export default AnimalsList;
