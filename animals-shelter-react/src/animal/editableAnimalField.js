import React from "react";
import { useMutation } from "@apollo/client";
import EditableFieldBase from "../common/editableFieldBase";
import { ANIMALS_QUERY, UPDATE_ANIMAL } from "../common/graphqlQueries";

const EditableAnimalField = ({ animal, ...props }) => {
  const [updateField] = useMutation(UPDATE_ANIMAL, {
    refetchQueries: [ANIMALS_QUERY],
  });

  return <EditableFieldBase {...props} entity={animal} updateField={updateField} />;
};

export default EditableAnimalField;
