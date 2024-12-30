import React, { useState, useEffect } from "react";
import { useMutation } from "@apollo/client";
import { Input, Button, Select, SelectSection, SelectItem } from "@nextui-org/react";
import { ANIMALS_QUERY, UPDATE_ANIMAL } from "../common/graphqlQueries.js";
import { DatePicker } from "@nextui-org/date-picker";
import { parseDate, getLocalTimeZone, today } from "@internationalized/date";
import { useDateFormatter } from "@react-aria/i18n";
import { GrFormAdd } from "react-icons/gr";
import { HiMinusSm } from "react-icons/hi";

const EditableAnimalField = ({ animal, value, name, values, isDate }) => {

  const [isEditing, setIsEditing] = useState(false);
  const [oldValue, setOldValue] = useState("");
  const [dat, setDat] = useState(isDate && value ? parseDate(value) : today());

  const [fieldValue, setFieldValue] = useState(value);

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
          <Select
            value={fieldValue}
            defaultSelectedKeys={[fieldValue]}
            isRequired
            aria-label="Editable"
            className="w-full md:w-28 editable-cell-field"
            onChange={(e) => setFieldValue(e.target.value)}>
            {values.map(v => (
              <SelectItem key={v}>{v}</SelectItem>
            ))}
          </Select>
        ) : isDate ? (
          <div className="flex w-full flex-wrap flex-nowrap gap-4">
            <DatePicker
              isRequired
              showMonthAndYearPickers
              className="max-w-[284px]"
              aria-label="Date Editable Field"
              value={dat}
              onChange={(e) => {
                setDat(e);
                setFieldValue(e.toString());
              }}
            />
          </div>
        ) : (
          <Input
            className="w-full md:w-28 editable-cell-field"
            value={fieldValue}
            onChange={(e) => setFieldValue(e.target.value)} />
        )
      ) : (
        <span
          onDoubleClick={() => {
            setOldValue(fieldValue);
            setIsEditing(true);
          }} >
          <Input
            isDisabled
            size="sm"
            variant="bordered"
            className="editable-cell-field"
            defaultValue={fieldValue}
            type="text"
          />
        </span>
      )}

      {isEditing && (
        <div className="flex items-center gap-0">
          <Button
            color="secondary"
            size="sm"
            variant="light"
            className="p-1 min-w-0 h-auto"
            onPress={handleSave}>
            <GrFormAdd />
          </Button>
          <Button
            color="secondary"
            size="sm"
            variant="light"
            className="p-1 min-w-0 h-auto"
            onPress={() => {
              setFieldValue(oldValue);
              setIsEditing(false);
            }}>
            <HiMinusSm />
          </Button>
        </div>
      )}
    </div>
  )
};

export default EditableAnimalField;
