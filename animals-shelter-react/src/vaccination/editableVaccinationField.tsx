import React from "react";
import { useMutation } from "@apollo/client";
import EditableFieldBase from "../common/editableFieldBase";
import { VACCINATIONS_QUERY, UPDATE_VACCINATION } from "../common/graphqlQueries";
import { EditableFieldProps } from "../common/types";

const EditableVaccineField: React.FC<EditableFieldProps> = ({ entity, ...props }) => {
    const [updateField] = useMutation(UPDATE_VACCINATION, {
        refetchQueries: [VACCINATIONS_QUERY],
    });

    return <EditableFieldBase {...props} entity={entity} updateField={updateField} />;
};

export default EditableVaccineField;
