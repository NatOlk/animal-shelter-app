import React from "react";
import { TableRow, TableCell } from "@nextui-org/react";
import EditableVaccinationField from "./editableVaccinationField";
import DeleteVaccination from "./deleteVaccination";
import { VaccinationRowProps } from "../common/types";

const VaccinationRow = ({ vaccination, onError }: VaccinationRowProps) => (
    <TableRow key={vaccination.id} className="table-row">
        <TableCell>{vaccination.id}</TableCell>
        <TableCell>{vaccination.vaccine}</TableCell>
        <TableCell>
            <EditableVaccinationField
            entity={vaccination}
            value={vaccination.batch}
            name="batch" />
        </TableCell>
        <TableCell>
            <EditableVaccinationField
            entity={vaccination}
            value={vaccination.vaccinationTime}
            name="vaccinationTime" isDate />
        </TableCell>
        <TableCell>
            <EditableVaccinationField
            entity={vaccination}
            value={vaccination.comments}
            name="comments" />
        </TableCell>
        <TableCell>
            <EditableVaccinationField
            entity={vaccination}
            value={vaccination.email}
            name="email" />
        </TableCell>
        <TableCell>
            <DeleteVaccination id={vaccination.id} onError={onError} />
        </TableCell>
    </TableRow>
);

export default VaccinationRow;
