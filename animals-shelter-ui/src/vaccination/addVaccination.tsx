import React, { useState } from "react";
import { TableRow, TableCell, Button, Select, SelectItem, Input } from "@nextui-org/react";
import { IoIosAddCircleOutline } from "react-icons/io";
import { useMutation } from "@apollo/client";
import { ADD_VACCINATION, VACCINATIONS_QUERY, ANIMALS_QUERY } from "../common/graphqlQueries";
import DateField from "../common/dateField";
import { today } from "@internationalized/date";
import { useAuth } from '../common/authContext';
import { Vaccination, AddVaccinationProps } from "../common/types";

const AddVaccination = ({ config, animalId, onError }: AddVaccinationProps) => {
    const { isAuthenticated, user } = useAuth();

    const initialValues: Vaccination = {
        id: "",
        vaccine: "Rabies",
        batch: "",
        comments: "Add new vaccine",
        vaccinationTime: today().toString(),
        email: isAuthenticated && user ? user.email : "",
        animal: { name: "", species: "" },
    };

    const [vaccination, setVaccination] = useState<Vaccination>(initialValues);

    const [addVaccination] = useMutation(ADD_VACCINATION, {
        refetchQueries: [
            { query: VACCINATIONS_QUERY, variables: { animalId } },
            { query: ANIMALS_QUERY }
        ],
    });

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setVaccination((prev) => ({ ...prev, [name]: value }));
    };

    const handleFieldChange = (fieldName: keyof Vaccination, value: string) => {
        setVaccination((prev) => ({ ...prev, [fieldName]: value }));
    };

    const handleAddVaccination = () => {
        addVaccination({
            variables: {
                vaccination: {
                    animalId,
                    vaccine: vaccination.vaccine,
                    batch: vaccination.batch,
                    vaccinationTime: vaccination.vaccinationTime,
                    comments: vaccination.comments,
                    email: vaccination.email
                }
            },
        }).catch((error) => {
            onError("Failed to add vaccination: " + error.message);
        });

        setVaccination(initialValues);
    };

    return (
        <TableRow key="add-vaccination-row" className="add-item-row highlighted-row">
            <TableCell></TableCell>
            <TableCell>
                <Select
                    name="vaccine"
                    defaultSelectedKeys={["Rabies"]}
                    className="w-full w-32"
                    aria-label="Vaccine"
                    onChange={handleInputChange}>
                    {config.vaccines.map((vaccine) => (
                        <SelectItem key={vaccine}>{vaccine}</SelectItem>
                    ))}
                </Select>
            </TableCell>
            <TableCell>
                <Input
                    name="batch"
                    value={vaccination.batch}
                    aria-label="Batch"
                    isRequired
                    onChange={handleInputChange}
                />
            </TableCell>
            <TableCell>
                <DateField
                    onDateChange={(newDate) => handleFieldChange("vaccinationTime", newDate.toString())} />
            </TableCell>
            <TableCell>
                <Input name="comments"
                value={vaccination.comments}
                onChange={handleInputChange} aria-label="Comments" />
            </TableCell>
            <TableCell>
                <Input
                    name="email"
                    value={vaccination.email}
                    isRequired
                    aria-label="Email"
                    onChange={handleInputChange}
                />
            </TableCell>
            <TableCell>
                <Button onPress={handleAddVaccination} color="default"
                    variant="light" className="p-2 min-w-2 h-auto">
                    <IoIosAddCircleOutline />
                </Button>
            </TableCell>
        </TableRow>
    );
};

export default AddVaccination;
