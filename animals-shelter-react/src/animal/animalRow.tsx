import React from "react";
import { TableRow, TableCell, Button } from "@nextui-org/react";
import { Link } from "react-router-dom";
import { BiInjection } from "react-icons/bi";
import DeleteAnimal from "./deleteAnimal";
import EditableAnimalField from "./editableAnimalField";
import { AnimalRowProps } from "../common/types";

const AnimalRow = ({ animal, config, onError }: AnimalRowProps) => (
    <TableRow key={animal.id} className="table-row">
        <TableCell>
            <Link
                to={`/animals/${animal.id}`}
                state={{ id: animal.id }}
                color="primary"
                className="animal-details-link">
                {animal.id}
            </Link>
        </TableCell>
        <TableCell>{animal.name}</TableCell>
        <TableCell>{animal.species}</TableCell>
        <TableCell>
            <EditableAnimalField
                entity={animal}
                value={animal.primaryColor}
                name="primaryColor"
                values={config.colors}/>
        </TableCell>
        <TableCell>
            <EditableAnimalField entity={animal} value={animal.breed} name="breed" />
        </TableCell>
        <TableCell>{animal.implantChipId}</TableCell>
        <TableCell>
            <EditableAnimalField
                entity={animal}
                value={animal.gender}
                name="gender"
                values={config.genders}/>
        </TableCell>
        <TableCell>
            <EditableAnimalField
                entity={animal}
                value={animal.birthDate}
                name="birthDate"
                isDate={true}/>
        </TableCell>
        <TableCell>
            <EditableAnimalField entity={animal} value={animal.pattern} name="pattern" />
        </TableCell>
        <TableCell>
            <div className="flex w-full flex-wrap flex-nowrap">
                <Button
                    as={Link}
                    to={`/vaccinations`}
                    state={{ animalId: animal.id, name: animal.name, species: animal.species }}
                    color="default"
                    variant="light"
                    className="p-2 min-w-2 h-auto">
                    {animal.vaccinationCount}
                    <BiInjection />
                </Button>
                &nbsp;
                <DeleteAnimal id={animal.id} onError={onError} />
            </div>
        </TableCell>
    </TableRow>
);

export default AnimalRow;
