import React from "react";
import { useMutation } from "@apollo/client";
import EditableFieldBase from "../common/editableFieldBase";
import { ANIMALS_QUERY, UPDATE_ANIMAL } from "../common/graphqlQueries";
import { EditableFieldProps } from "../common/types";

const EditableAnimalField: React.FC<EditableFieldProps> = ({ entity, ...props }) => {
  const [updateField] = useMutation(UPDATE_ANIMAL, {
    refetchQueries: [{ query: ANIMALS_QUERY }],
  });

  const handleUpdate = async (updatedField: Record<string, any>) => {
    if (!entity || !entity.id) {
      return;
    }

    const updatedData = {
      animal: {
        id: entity.id,
        ...updatedField
      }
    };

    await updateField({
        variables: updatedData
      });
  };
  return <EditableFieldBase {...props} entity={entity} updateField={handleUpdate} />;
};

export default EditableAnimalField;
