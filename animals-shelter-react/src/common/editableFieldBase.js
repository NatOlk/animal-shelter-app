import React, { useState } from "react";
import { Input, Button, Select, SelectItem } from "@nextui-org/react";
import { Popover, PopoverContent } from "@nextui-org/react";
import { DatePicker } from "@nextui-org/date-picker";
import { GrFormAdd } from "react-icons/gr";
import { HiMinusSm } from "react-icons/hi";
import { parseDate, today } from "@internationalized/date";

const EditableFieldBase = ({
  entity,
  value,
  name,
  values,
  isDate,
  updateField,
}) => {
  const [isEditing, setIsEditing] = useState(false);
  const [oldValue, setOldValue] = useState("");
  const [dat, setDat] = useState(isDate && value ? parseDate(value) : today());
  const [fieldValue, setFieldValue] = useState(value);
  const [error, setError] = useState("");

  const handleSave = () => {
    const variables = {
      id: entity.id,
      [name]: fieldValue,
    };

    updateField({ variables }).catch((err) => {
      setError(err.message);
      setTimeout(() => setError(""), 15000);
    });
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
            {values.map((v) => (
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
              }} />
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
          }}>
          <Input
            isDisabled
            size="sm"
            variant="bordered"
            className="editable-cell-field"
            defaultValue={fieldValue}
            type="text" />
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

      {error && (
        <Popover isOpen onOpenChange={() => setError("")}>
          <PopoverContent>
            <p>{error}</p>
          </PopoverContent>
        </Popover>
      )}
    </div>
  );
};

export default EditableFieldBase;
