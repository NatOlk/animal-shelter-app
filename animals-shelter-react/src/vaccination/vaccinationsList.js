import React, { useState, useEffect } from 'react';
import { useQuery, useMutation } from "@apollo/client";
import { useAsyncList } from "@react-stately/data";
import {
    Select, SelectItem, Spinner,
    Table, TableHeader, TableColumn, TableBody, TableRow, TableCell,
    Button, Input, Pagination, Progress
} from "@nextui-org/react";
import DeleteVaccination from "./deleteVaccination";
import EditableVaccinationField from './editableVaccinationField';
import { useLocation, Link } from 'react-router-dom';
import { VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY, ADD_VACCINATION } from '../common/graphqlQueries.js';
import DateField from '../common/dateField';
import { useAuth } from '../common/authContext';
import { useConfig } from '../common/configContext';
import { IoIosAddCircleOutline } from "react-icons/io"

function VaccinationsList() {
    const perPage = 8;
    const [currentPage, setCurrentPage] = useState(0);
    const location = useLocation();
    const { animalId } = location.state;

    if (!animalId) {
        return (
            <div>
                <Link to="/">Back to Animals</Link>
                <p>Error: No animalId provided!</p>
            </div>
        );
    }

    const { isAuthenticated, user } = useAuth();
    console.log('Is auth user ' + user + '-' + isAuthenticated);
    const initialValues = {
        vaccine: 'Rabies',
        batch: '',
        comments: 'Add new vaccine',
        vaccinationTime: today().toString(),
        email: isAuthenticated && user ? user.email : '',
    };

    const [vaccination, setVaccination] = useState(initialValues);

    const [addVaccination] = useMutation(ADD_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY],
        update(cache, { data: { addVaccination } }) {
            try {
                const { allVaccinations } = cache.readQuery({ query: ALL_VACCINATIONS_QUERY });
                cache.writeQuery({
                    query: ALL_VACCINATIONS_QUERY,
                    data: { allVaccinations: [...allVaccinations, addVaccination] },
                });
            } catch (error) {
                console.error("Error updating cache:", error);
            }
        }
    });

    const config = useConfig();
    if (config.config == null) return <p>Loading configs...</p>;

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setVaccination({
            ...vaccination,
            [name]: value,
        });
    };

    const handleFieldChange = (fieldName, value) => {
        setVaccination({
            ...vaccination,
            [fieldName]: value,
        });
    };

    const handleAddVaccination = () => {
        addVaccination({
            variables: {
                animalId: animalId,
                vaccine: vaccination.vaccine,
                batch: vaccination.batch,
                vaccinationTime: vaccination.vaccinationTime,
                comments: vaccination.comments,
                email: vaccination.email
            }
        }).catch(error => { showError({ error: error }) });

        setVaccination(initialValues);
    }

    const { loading, error, data } = useQuery(VACCINATIONS_QUERY, {
        variables: { animalId: animalId },
        fetchPolicy: 'network-only',
    });

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

    if (loading) return (
        <div>
            <p> Loading...</p>
            <Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />
        </div>
    );
    if (error) return <p>Error :(</p>;

    const formatDate = (dateString) => {
        if (!dateString) return "";
        const date = new Date(dateString);
        return date.toISOString().slice(0, 10);
    };

    const vaccinationsList = list.items
        .slice(currentPage * perPage, (currentPage + 1) * perPage);

    const pageCount = Math.ceil(list.items.length / perPage);

    return (
        <div>
            <div id="error" className="errorAlarm"></div>
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
                    <TableRow className="add-item-row highlighted-row">
                        <TableCell></TableCell>
                        <TableCell>
                            <Select
                                name="vaccine" value={vaccination.vaccine}
                                className="w-full md:w-32"
                                aria-label="Vaccine"
                                onChange={handleInputChange}>
                                {config.config.vaccines.map(vaccine => (
                                    <SelectItem key={vaccine}>{vaccine}</SelectItem>
                                ))}
                            </Select>
                        </TableCell>
                        <TableCell>
                            <Input name="batch"
                                value={vaccination.batch}
                                aria-label="Batch"
                                isRequired
                                onChange={handleInputChange} />
                        </TableCell>
                        <TableCell>
                            <DateField onDateChange={(newDate) =>
                                handleFieldChange("vaccinationTime", newDate.toString())} />
                        </TableCell>
                        <TableCell>
                            <Input name="comments" value={vaccination.comments}
                                onChange={handleInputChange}
                                aria-label="Comments" />
                        </TableCell>
                        <TableCell>
                            <Input name="email" value={vaccination.email}
                                isRequired
                                aria-label="Email"
                                onChange={handleInputChange} />
                        </TableCell>
                        <TableCell>
                            <Button onPress={handleAddVaccination}
                                color="default" variant="light"
                                className="p-2 min-w-2 h-auto">
                                <IoIosAddCircleOutline />
                            </Button>
                        </TableCell>
                    </TableRow>
                    {vaccinationsList.map((vaccination, index) => (
                        <TableRow key={vaccination.id}>
                            <TableCell>{vaccination.id}</TableCell>
                            <TableCell>{vaccination.vaccine}</TableCell>
                            <TableCell>
                                <EditableVaccinationField
                                    vaccination={vaccination}
                                    value={vaccination.batch}
                                    name="email" />
                            </TableCell>
                            <TableCell>
                                <EditableVaccinationField
                                    vaccination={vaccination}
                                    value={vaccination.vaccinationTime}
                                    name="vaccinationTime"
                                    isDate={true} />
                            </TableCell>
                            <TableCell>
                                <EditableVaccinationField
                                    vaccination={vaccination}
                                    value={vaccination.comments}
                                    name="comments" />
                            </TableCell>
                            <TableCell>
                                <EditableVaccinationField
                                    vaccination={vaccination}
                                    value={vaccination.email}
                                    name="email" />
                            </TableCell>
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

export default VaccinationsList;
