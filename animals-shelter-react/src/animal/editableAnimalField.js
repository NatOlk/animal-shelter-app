import React, { useState, useEffect } from "react";
import { useMutation } from "@apollo/client";
import { Input, Button } from "@nextui-org/react";
import { Dropdown, DropdownMenu, DropdownItem } from "@nextui-org/dropdown";
import { ANIMALS_QUERY, UPDATE_ANIMAL } from "../common/graphqlQueries.js";

const EditableAnimalField = ({ animal, value, name, values, isDate }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [fieldValue, setFieldValue] = useState(value);
  const [oldValue, setOldValue] = useState("");

  const [updateField] = useMutation(UPDATE_ANIMAL, {
    refetchQueries: [ANIMALS_QUERY],
    onCompleted: () => setIsEditing(false),
  });

  const handleSave = () => {
    const variables = {
      id: animal.id,
      [name]: fieldValue,
    };

    updateField({ variables }).catch((err) => console.error(err));
  };

  return (
    <div className="flex items-center gap-3">
      {isEditing ? (
        values && values.length > 0 ? (
          <Dropdown>
            <DropdownMenu aria-label="Dropdown Variants" variant="bordered">
              {values.map((val) => (
                <DropdownItem
                  key={val}
                  onClick={() => setFieldValue(val)}>
                  {val}
                </DropdownItem>
              ))}
            </DropdownMenu>
          </Dropdown>
        ) : isDate ? (
          <Input
            type="date"
            value={fieldValue}
            onChange={(e) => setFieldValue(e.target.value)}
          />
        ) : (
          <Input
            value={fieldValue}
            onChange={(e) => setFieldValue(e.target.value)}
          />
        )
      ) : (
        <span
          onDoubleClick={() => {
            setOldValue(fieldValue);
            setIsEditing(true);
          }}
          className="cursor-pointer text-primary">
          {fieldValue}
        </span>
      )}

      {isEditing && (
        <>
          <Button
            color="success"
            size="sm"
            onClick={handleSave}>
            +
          </Button>
          <Button
            color="error"
            size="sm"
            onClick={() => {
              setFieldValue(oldValue);
              setIsEditing(false);
            }}>
            -
          </Button>
        </>
      )}
    </div>
  )
};

export default EditableAnimalField;
