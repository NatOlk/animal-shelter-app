import React, { useState, useEffect } from 'react';
import { useMutation } from '@apollo/client';
import { VACCINATIONS_QUERY, UPDATE_VACCINATION } from '../common/graphqlQueries.js';
import { DatePicker } from "@nextui-org/date-picker";
import { parseDate, getLocalTimeZone, today } from "@internationalized/date";
import { useDateFormatter } from "@react-aria/i18n";
import { Input, Button, Select, SelectSection, SelectItem } from "@nextui-org/react";

const EditableVaccineField = ({ vaccination, value, name, values, isDate }) => {
  const [isEditing, setIsEditing] = useState(false);

  const [oldValue, setOldValue] = useState("");
  const [dat, setDat] = useState(isDate && value ? parseDate(value) : today());

  const [fieldValue, setFieldValue] = useState(value);

  const [updateField] = useMutation(UPDATE_VACCINATION, {
    refetchQueries: [VACCINATIONS_QUERY],
    onCompleted: () => setIsEditing(false),
  });

  const handleSave = () => {
    const variables = {
      id: vaccination.id,
      [name]: fieldValue,
    };

    updateField({ variables }).catch(err => console.error(err));
  };

  const inputStyle = isEditing ? "red-background" : "editable-field";
  const combinedClassName = `${inputStyle} browser-default`;

  return (
    <div className="flex items-center gap-3">
      {isEditing ? (
        values && values.length > 0 ? (
          <Select
            value={fieldValue}
            defaultSelectedKeys={[fieldValue]}
            isRequired
            className="w-full md:w-28"
            onChange={(e) => setFieldValue(e.target.value)}>
            {values.map((val) => (
              <SelectItem key={val}>
                {val}
              </SelectItem>
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
            className="w-full md:w-28"
            value={fieldValue}
            onChange={(e) => setFieldValue(e.target.value)} />
        )
      ) : (
        <span
          className="w-full md:w-28"
          onDoubleClick={() => {
            setOldValue(fieldValue);
            setIsEditing(true);
          }}>
          {fieldValue}
        </span>
      )}
      {isEditing && (
        <>
          <Button
            color="success"
            size="sm"
            onPress={handleSave}>
            +
          </Button>
          <Button
            color="error"
            size="sm"
            onPress={() => {
              setFieldValue(oldValue);
              setIsEditing(false);
            }}>
            -
          </Button>
        </>
      )}
    </div>
  );
};

export default EditableVaccineField;
