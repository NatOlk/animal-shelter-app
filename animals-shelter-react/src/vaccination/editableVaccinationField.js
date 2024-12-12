import React, { useState, useEffect } from 'react';
import { useMutation } from '@apollo/client';
import { VACCINATIONS_QUERY, UPDATE_VACCINATION } from '../common/graphqlQueries.js';
import { DatePicker } from "@nextui-org/date-picker";
import { parseDate, getLocalTimeZone } from "@internationalized/date";
import { useDateFormatter } from "@react-aria/i18n";
import { Input, Button, Select, SelectSection, SelectItem } from "@nextui-org/react";

const EditableVaccineField = ({ vaccination, value, name, values, isDate }) => {
  const [isEditing, setIsEditing] = useState(false);

  const [oldValue, setOldValue] = useState("");
  const [dat, setDat] = useState(isDate && value ? new Date(value) : new Date());
  console.log('Dat = ' + dat);

  const [birthDate, setBirthDate] = useState(parseDate(dat.toISOString().split('T')[0]));
console.log('BD = ' + birthDate);

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const d = date.toLocaleDateString("en-US", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
    console.log('String ' + dateString + ';new date ' + date + '; date ' + d)
    return d;
  };

  const [fieldValue, setFieldValue] = useState(isDate && value ? formatDate(birthDate) : value);

   const formatter = new Intl.DateTimeFormat("en-US", {
           day: "2-digit",
           month: "2-digit",
           year: "numeric",
       });

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
          <div className="flex w-full flex-wrap flex-nowrap">
            <DatePicker
              isRequired
              value={birthDate}
              onChange={(e) => {
                console.log('e = ' + e);
                if (e) {
                  const formattedDate = formatter.format(e.toDate(getLocalTimeZone()));
                  console.log('Formated date = ' + formattedDate);
                  setFieldValue(formattedDate);
                  setBirthDate(e);
                } else {
                  setFieldValue("");
                }
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
