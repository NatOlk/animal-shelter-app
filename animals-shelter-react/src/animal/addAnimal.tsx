import React, { useState } from "react";
import { TableRow, TableCell, Button, Select, SelectItem, Input } from "@nextui-org/react";
import { IoIosAddCircleOutline } from "react-icons/io";
import { useMutation } from "@apollo/client";
import { ADD_ANIMAL, ANIMALS_QUERY } from "../common/graphqlQueries";
import DateField from "../common/dateField";
import { today } from "@internationalized/date";
import { Animal, AddAnimalProps } from "../common/types";

const AddAnimal = ({ config, onError }: AddAnimalProps) => {
    const initialValues: Animal = {
        id: "",
        name: "",
        species: "Cat",
        primaryColor: "White",
        gender: "F",
        breed: "",
        birthDate: today().toString(),
        implantChipId: "00000000-00000000-0000",
        pattern: "Broken",
    };

    const [animal, setAnimal] = useState<Animal>(initialValues);

    const [addAnimal] = useMutation(ADD_ANIMAL, {
        refetchQueries: [{ query: ANIMALS_QUERY }],
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setAnimal((prevAnimal) => ({
            ...prevAnimal,
            [name]: value,
        }));
    };

    const handleFieldChange = (fieldName, value) => {
        setAnimal((prevAnimal) => ({
            ...prevAnimal,
            [fieldName]: value,
        }));
    };

    const handleAddAnimal = () => {
        addAnimal({
            variables: {
                animal: {
                    name: animal.name,
                    species: animal.species,
                    primaryColor: animal.primaryColor,
                    breed: animal.breed,
                    implantChipId: animal.implantChipId,
                    gender: animal.gender,
                    birthDate: animal.birthDate,
                    pattern: animal.pattern
                }
            }
        }).catch((error) => {
            setGlobalError("Failed to add animal: " + error.message);
            setTimeout(() => setGlobalError(""), 15000);
        });
        setAnimal(initialValues);
    };

    return (
        <TableRow key="add-animal-row" className="add-item-row highlighted-row">
            <TableCell></TableCell>
            <TableCell>
                <Input
                    name="name"
                    value={animal.name}
                    className="w-full w-32"
                    aria-label="Animal Name"
                    onChange={handleInputChange}
                    isRequired />
            </TableCell>
            <TableCell>
                <Select
                    name="species"
                    defaultSelectedKeys={["Cat"]}
                    className="w-full w-28"
                    variant="bordered"
                    isRequired
                    aria-label="Animal Species"
                    onChange={handleInputChange}>
                    {config.species.map((species) => (
                        <SelectItem key={species}>{species}</SelectItem>
                    ))}
                </Select>
            </TableCell>
            <TableCell>
                <Select
                    name="primaryColor"
                    className="w-full w-28"
                    defaultSelectedKeys={["White"]}
                    isRequired
                    aria-label="Animal Primary Color"
                    onChange={handleInputChange}>
                    {config.colors.map((color) => (
                        <SelectItem key={color}>{color}</SelectItem>
                    ))}
                </Select>
            </TableCell>
            <TableCell>
                <Input
                    name="breed"
                    value={animal.breed}
                    onChange={handleInputChange}
                    className="w-full w-24"
                    aria-label="Animal Breed" />
            </TableCell>
            <TableCell>
                <Input
                    name="implantChipId"
                    value={animal.implantChipId}
                    isRequired
                    className="w-full w-36"
                    onChange={handleInputChange}
                    aria-label="Animal Implant Chip" />
            </TableCell>
            <TableCell>
                <Select
                    name="gender"
                    defaultSelectedKeys={["F"]}
                    className="w-full w-16"
                    isRequired
                    aria-label="Animal Gender"
                    onChange={handleInputChange}>
                    {config.genders.map((gender) => (
                        <SelectItem key={gender}>{gender}</SelectItem>
                    ))}
                </Select>
            </TableCell>
            <TableCell>
                <DateField
                    onDateChange={(newDate) =>
                        handleFieldChange("birthDate", newDate.toString())
                    } />
            </TableCell>
            <TableCell>
                <Input
                    name="pattern"
                    value={animal.pattern}
                    className="w-full w-24"
                    aria-label="Animal Pattern"
                    onChange={handleInputChange} />
            </TableCell>
            <TableCell>
                <Button
                    onPress={handleAddAnimal}
                    color="default"
                    variant="light"
                    className="p-2 min-w-2 h-auto">
                    <IoIosAddCircleOutline />
                </Button>
            </TableCell>
        </TableRow>
    );
};

export default AddAnimal;
