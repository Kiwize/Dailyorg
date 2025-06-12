import { Box } from "@mui/material";
import Header from "../../Header";
import { Typography } from "@mui/material";
import WeekViewCalendar from "./WeekViewCalendar";
import React, { useState } from "react";

function DOHomePage() {
    const [selectedDate, setSelectedDate] = useState(new Date());

    const handleDateSelect = (date) => {
        setSelectedDate(date);
        console.log("Selected date:", date);
    };

    return (
        <Box>
            <Header/>
            <Typography variant="h4" sx={{ my: 2 }}>Daily Organizer</Typography>
            <Box sx={{ display: 'flex', flexDirection: 'row' , justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <WeekViewCalendar onDateSelect={handleDateSelect}/>
            </Box>
        </Box>
    );
}

export default DOHomePage