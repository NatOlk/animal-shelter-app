import React, { useState } from "react";
import {
    Input, Button, Select, SelectItem,
    Popover, PopoverContent
} from "@nextui-org/react";
import { DatePicker } from "@nextui-org/date-picker";
import { GrFormAdd } from "react-icons/gr";
import { HiMinusSm } from "react-icons/hi";
import { parseDate, today } from "@internationalized/date";
import { EditableFieldBaseProps } from "./types";

const EditableFieldBase: React.FC<EditableFieldBaseProps> = ({
    entity,
    value,
    name,
    values,
    isDate,
    updateField,
}) => {
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [oldValue, setOldValue] = useState<string>("");
    const [dat, setDat] = useState(isDate && value ? parseDate(value) : today());
    const [fieldValue, setFieldValue] = useState<string>(value);
    const [error, setError] = useState<string>("");

    const handleSave = () => {
        if (!updateField) {
            return;
        }

        updateField({ [name]: fieldValue })
            .then(() => {
                setIsEditing(false);
            })
            .catch((err) => {
                setError(err.message);
                setTimeout(() => setError(""), 15000);
            });
    };

    const renderInputField = () => {
        if (values && values.length > 0) {
            return (
                <Select
                    value={fieldValue}
                    defaultSelectedKeys={[fieldValue]}
                    isRequired
                    aria-label="Editable"
                    className="w-full w-28 editable-cell-field"
                    onChange={(e) => setFieldValue(e.target.value)}>
                    {values.map((v) => (
                        <SelectItem key={v}>{v}</SelectItem>
                    ))}
                </Select>
            );
        }

        if (isDate) {
            return (
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
                        }}/>
                </div>
            );
        }

        return (
            <Input
                className="w-full w-28 editable-cell-field"
                value={fieldValue}
                onChange={(e) => setFieldValue(e.target.value)}
            />
        );
    };

    return (
        <div className="flex items-center gap-3">
            {isEditing ? (
                renderInputField()
            ) : (
                <button
                    type="button"
                    className="editable-span-button"
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
                        type="text"/>
                </button>
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
