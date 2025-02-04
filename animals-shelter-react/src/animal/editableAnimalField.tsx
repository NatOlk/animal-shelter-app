import React from "react";
import { useMutation } from "@apollo/client";
import EditableFieldBase from "../common/editableFieldBase";
import { ANIMALS_QUERY, UPDATE_ANIMAL } from "../common/graphqlQueries";
import { EditableFieldProps } from "../common/types";

const EditableAnimalField: React.FC<EditableFieldProps> = ({ entity, ...props }) => {
  const [updateField] = useMutation(UPDATE_ANIMAL, {
    refetchQueries: [ANIMALS_QUERY],
  });

  return <EditableFieldBase {...props} entity={entity} updateField={updateField} />;
};

export default EditableAnimalField;
