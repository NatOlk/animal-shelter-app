import React, { useState } from "react";
import { DatePicker } from "@nextui-org/date-picker";
import { getLocalTimeZone, today } from "@internationalized/date";
import { DateFieldProps } from "./types";

const DateField: React.FC<DateFieldProps> = ({ onDateChange }) => {
    const [date, setDate] = useState(today(getLocalTimeZone()));

    const handleChange = (newDate: any) => {
        setDate(newDate);
        if (onDateChange) {
            onDateChange(newDate);
        }
    };

    return (
        <DatePicker
            isRequired
            className="max-w-[284px]"
            showMonthAndYearPickers
            name="date"
            value={date}
            aria-label="Date Field"
            onChange={handleChange}
        />
    );
};

export default DateField;
