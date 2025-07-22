import { Box, Grid2 } from '@mui/material';
import Header from '../../Header';
import { Typography } from '@mui/material';
import WeekViewCalendar from './WeekViewCalendar';
import TasksCompletedGauge from './TasksCompletedGauge';
import { isSameDay, set } from 'date-fns';
import React from 'react';

const today = new Date();

function DOHomePage() {
  // Today's stats
  const [todayNumberOfTasks, setTodayNumberOfTasks] = React.useState(0);
  const [todayCompletedTasks, setTodayCompletedTasks] = React.useState(0);

  const handleCalendarWeekTaskRefresh = (tasks) => {
    // Callback function called AFTER the week tasks have been refreshed
    var todayNumberOfTasks = 0;
    var todayCompletedTasks = 0;

    console.log('Refreshing tasks for today:', tasks);

    var todayTasks = tasks.filter((task) => {
      return isSameDay(new Date(task.taskStartDate), today);
    });

    console.log('Tasks for the week have been refreshed:', todayTasks);
    todayNumberOfTasks = todayTasks.length;
    todayCompletedTasks = todayTasks.filter((task) => task.taskCompleted).length;

    console.log("Today's number of tasks:", todayNumberOfTasks);
    console.log("Today's completed tasks:", todayCompletedTasks);

    setTodayNumberOfTasks(todayNumberOfTasks);
    setTodayCompletedTasks(todayCompletedTasks);
  };

  return (
    <Box>
      <Header />
      <Typography variant="h4" sx={{ my: 2 }}>
        Daily Organizer
      </Typography>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'row',
          justifyContent: 'space-between',
          alignItems: 'center',
          mb: 2,
        }}
      >
        <WeekViewCalendar calendarRefreshCallback={handleCalendarWeekTaskRefresh} />
      </Box>

      <Grid2 container spacing={2} sx={{ mt: 2 }}>
        {/* Widgets */}
        <Grid2 size={{ xs: 12, sm: 6, md: 4 }}>
          {todayNumberOfTasks > 0 ? (
            <Typography variant="h6" sx={{ mb: 1 }}>
              <TasksCompletedGauge totalTasks={todayNumberOfTasks} completedTasks={todayCompletedTasks} />
            </Typography>
          ) : (
            <Typography variant="h6" sx={{ mb: 1 }}>
              No tasks for today 👍
            </Typography>
          )}
        </Grid2>
      </Grid2>
    </Box>
  );
}

export default DOHomePage;
