import React, { useState } from "react";
import {
  format,
  startOfWeek,
  addDays,
  addWeeks,
  subWeeks,
  isSameDay,
} from "date-fns";
import { Box, Button, Typography, Paper } from "@mui/material";

export default function WeekViewCalendar({ onDateSelect }) {
  const [currentDate, setCurrentDate] = useState(new Date());
  const startOfCurrentWeek = startOfWeek(currentDate, { weekStartsOn: 0 }); // Sunday

  const weekDays = [...Array(7)].map((_, i) => {
    const date = addDays(startOfCurrentWeek, i);
    return {
      date,
      label: format(date, "EEE dd"),
      isToday: isSameDay(date, new Date()),
    };
  });

  const goToPreviousWeek = () => setCurrentDate(subWeeks(currentDate, 1));
  const goToNextWeek = () => setCurrentDate(addWeeks(currentDate, 1));

  // Example task object placeholder
  const task = {
    title: "Sample Task",
    start: new Date(
      currentDate.getFullYear(),
      currentDate.getMonth(),
      currentDate.getDate(),
      9,
      0
    ), // 9:00 AM
    end: new Date(
      currentDate.getFullYear(),
      currentDate.getMonth(),
      currentDate.getDate(),
      11,
      30
    ), // 11:30 AM
  };

  // Helper to get top and height percentages for a task
  function getTaskPosition(start, end) {
    const startHour = start.getHours() + start.getMinutes() / 60;
    const endHour = end.getHours() + end.getMinutes() / 60;
    const top = (startHour / 24) * 100;
    const height = ((endHour - startHour) / 24) * 100;
    return { top: `${top}%`, height: `${height}%` };
  }

  return (
    <Box className="h-full" sx={{ margin: "0 auto", p: 2 }}>
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        mb={2}
      >
        <Button variant="outlined" onClick={goToPreviousWeek}>
          ‹ Prev
        </Button>
        <Typography variant="h6">
          {format(startOfCurrentWeek, "MMMM yyyy")}
        </Typography>
        <Button variant="outlined" onClick={goToNextWeek}>
          Next ›
        </Button>
      </Box>
      <Box
        className="grid h-full"
        gridTemplateColumns="repeat(7, 1fr)"
        gap={1}
        sx={{
          height: "100%",
          minHeight: { md: "300px" },
        }}
      >
        {weekDays.map(({ date, label, isToday }) => (
          <Paper
            key={date.toISOString()}
            elevation={isToday ? 4 : 1}
            sx={{
              position: "relative",
              padding: 2,
              backgroundColor: isToday ? "primary.dark" : "background.paper",
              border: isToday ? "2px solid" : "1px solid",
              borderColor: isToday ? "primary.main" : "grey.300",
              textAlign: "center",
              height: { xs: "auto", md: "40vh" },
              minHeight: { md: "40%" },
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
              overflow: "hidden",
            }}
            onClick={() => onDateSelect(date)}
          >
            <Typography variant="body1">{label}</Typography>
            {/* Render the task only if it matches this date */}
            {isSameDay(date, task.start) && (
              <div
                style={{
                  position: "absolute",
                  left: 2,
                  right: 2,
                  ...getTaskPosition(task.start, task.end),
                  background: "#1976d2",
                  color: "#fff",
                  borderRadius: 4,
                  padding: "2px 6px",
                  fontSize: 12,
                  zIndex: 2,
                  boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
                  display: "flex",
                  alignItems: "center",
                }}
              >
                {task.title}
              </div>
            )}
          </Paper>
        ))}
      </Box>
    </Box>
  );
}
