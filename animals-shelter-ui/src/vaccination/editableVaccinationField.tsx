import React from "react";
import { useMutation } from "@apollo/client";
import EditableFieldBase from "../common/editableFieldBase";
import { VACCINATIONS_QUERY, UPDATE_VACCINATION } from "../common/graphqlQueries";
import { EditableFieldProps } from "../common/types";

const EditableVaccineField: React.FC<EditableFieldProps> = ({ entity, ...props }) => {
  const [updateField] = useMutation(UPDATE_VACCINATION, {
    refetchQueries: [{ query: VACCINATIONS_QUERY }],
  });

  const handleUpdate = async (updatedField: Record<string, any>) => {
    if (!entity || !entity.id) {
      return;
    }

    const updatedData = {
      vaccination: {
        id: entity.id,
        ...updatedField
      }
    };

    await updateField({
      variables: updatedData
    });
  };

  return <EditableFieldBase entity={entity} updateField={handleUpdate} {...props} />;
};

export default EditableVaccineField;
