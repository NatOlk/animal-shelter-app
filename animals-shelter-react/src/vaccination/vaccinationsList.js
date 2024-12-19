import React, { useState, useEffect } from 'react';
import { useQuery } from "@apollo/client";
import { useMutation } from "@apollo/client";
import DeleteVaccination from "./deleteVaccination";
import EditableVaccinationField from './editableVaccinationField';
import { useLocation, Link } from 'react-router-dom';
import { VACCINATIONS_QUERY, ALL_VACCINATIONS_QUERY, ADD_VACCINATION } from '../common/graphqlQueries.js';
import Pagination from '../common/pagination'
import {
    Select, SelectSection, SelectItem, Spacer,
    Table, TableHeader, TableColumn, TableBody, TableRow, TableCell,
    Button, Input
} from "@nextui-org/react";
import { DatePicker } from "@nextui-org/date-picker";
import { parseDate, getLocalTimeZone } from "@internationalized/date";
import { useDateFormatter } from "@react-aria/i18n";
import { useAuth } from '../common/authContext';
import { useConfig } from '../common/configContext';
import { IoIosAddCircleOutline } from "react-icons/io"

function VaccinationsList() {
    const perPage = 10;
    const [currentPage, setCurrentPage] = useState(0);

    const location = useLocation();
    const { animalId, name, species } = location.state;
    const [birthDate, setBirthDate] = React.useState(parseDate("2024-04-16"));

    const formatter = new Intl.DateTimeFormat("en-US", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
    });
    const [vaccination, setVaccination] = useState({
        vaccine: 'Rabies',
        batch: '',
        vaccinationTime: new Date().toISOString().split('T')[0],
        comments: 'Add new vaccine',
        email: ''
    });
    const [validationError, setError] = useState(null);
    const { isAuthenticated, user } = useAuth();

    useEffect(() => {
        if (isAuthenticated && user) {
            setVaccination((prev) => ({ ...prev, email: user.email }));
        }
    }, [isAuthenticated, user]);

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
    if (!config) {
        return (
            <tr>
                <td colSpan="5">Loading animals configs...</td>
            </tr>
        );
    }

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setVaccination({
            ...vaccination,
            [name]: value,
        });
    };

    const handleAddVaccination = () => {
        if (!vaccination.vaccine) {
            setError('vaccine');
            return;
        }
        if (!vaccination.batch) {
            setError('batch');
            return;
        }
        if (!vaccination.vaccinationTime) {
            setError('vaccinationTime');
            return;
        }

        addVaccination({
            variables: {
                animalId: animalId,
                vaccine: vaccination.vaccine,
                batch: vaccination.batch,
                vaccinationTime: birthDate ? formatter.format(birthDate.toDate(getLocalTimeZone())) : "01/01/2025",
                comments: vaccination.comments,
                email: vaccination.email
            }
        }).catch(error => { showError({ error: error }) });

        clearFields();
    }

    const clearFields = () => {
        setVaccination({
            vaccine: 'Rabies',
            batch: '',
            vaccinationTime: new Date().toISOString().split('T')[0],
            comments: 'Add new vaccine',
            email: user.email
        });
    };

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
            <Table className="compact-table">
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
                    <TableRow className="highlighted-row">
                        <TableCell></TableCell>
                        <TableCell>
                            <Select
                                name="vaccine" value={vaccination.vaccine}
                                className="w-full md:w-32"
                                onChange={handleInputChange}>
                                {config.config.vaccines.map(vaccine => (
                                    <SelectItem key={vaccine}>{vaccine}</SelectItem>
                                ))}
                            </Select>
                        </TableCell>
                        <TableCell>
                            <Input name="batch"
                                value={vaccination.batch}
                                onChange={handleInputChange}
                                placeholder={validationError === 'batch' ? 'batch is mandatory' : ''} />
                        </TableCell>
                        <TableCell>
                            <DatePicker isRequired className="max-w-[284px]"
                                name="birthDate" value={birthDate}
                                onChange={setBirthDate} />
                        </TableCell>
                        <TableCell>
                            <Input name="comments" value={vaccination.comments}
                                onChange={handleInputChange} />
                        </TableCell>
                        <TableCell>
                            <Input name="email" value={vaccination.email} onChange={handleInputChange} />
                        </TableCell>
                        <TableCell>
                            <Button onPress={handleAddVaccination}
                                color="default" size="sm">
                                <IoIosAddCircleOutline className="h-4 w-4" />
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
                currentPage={currentPage}
                pageCount={pageCount}
                onPageChange={setCurrentPage} />
        </div>
    );
}

export default VaccinationsList;
