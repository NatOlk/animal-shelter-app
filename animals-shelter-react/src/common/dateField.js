import React, { useState, useEffect } from "react";
import { DatePicker } from "@nextui-org/date-picker";
import { parseDate, getLocalTimeZone, today } from "@internationalized/date";
import { useDateFormatter } from "@react-aria/i18n";

const DateField = ({ onDateChange }) => {
    let [date, setDate] =  React.useState(today(getLocalTimeZone()));

    const handleChange = (newDate) => {
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
