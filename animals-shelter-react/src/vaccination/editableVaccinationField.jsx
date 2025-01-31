import React from "react";
import { useMutation } from "@apollo/client";
import EditableFieldBase from "../common/editableFieldBase";
import { VACCINATIONS_QUERY, UPDATE_VACCINATION } from "../common/graphqlQueries";

const EditableVaccineField = ({ vaccination, ...props }) => {
  const [updateField] = useMutation(UPDATE_VACCINATION, {
    refetchQueries: [VACCINATIONS_QUERY],
  });

  return <EditableFieldBase {...props} entity={vaccination} updateField={updateField} />;
};

export default EditableVaccineField;
