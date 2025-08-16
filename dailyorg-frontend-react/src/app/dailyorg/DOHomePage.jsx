import { Box, Grid2 } from '@mui/material';
import Header from '../../Header';
import { Typography } from '@mui/material';
import WeekViewCalendar from './WeekViewCalendar';
import TasksCompletedGauge from './TasksCompletedGauge';
import { isSameDay, set } from 'date-fns';
import React from 'react';
import { useToolbar } from '../../contexts/ToolbarProvider';
import DOSettings from './DOSettings';

const today = new Date();

function DOHomePage() {
  // Today's stats
  const [todayNumberOfTasks, setTodayNumberOfTasks] = React.useState(0);
  const [todayCompletedTasks, setTodayCompletedTasks] = React.useState(0);

  // Ref to check if local settings are opened
  const [areLocalSettingsOpened, setLocalSettingsOpened] = React.useState(false);

  const toolBar = useToolbar();

  //Daily Organizer Settings
  //Load those from DB later
  const [settings, setSettings] = React.useState({
    firstDisplayedHour: 8,
    displayedHours: 12,
  });

  //Initializing the toolbar
  React.useEffect(() => {
    // Set the toolbar title and actions
    const toolbarTitle = 'Daily Organizer';
    const toolbarActions = [
      {
        label: 'Settings',
        onClick: () => {
          setLocalSettingsOpened((prev) => {
            return !prev;
          });
        },
      },
      {
        label: 'Help',
        onClick: () => console.log('Help clicked'),
      },
    ];
    toolBar.setToolbarEnabled(true);
    toolBar.updateToolbarTitle(toolbarTitle);
    toolBar.updateToolbarActions(toolbarActions);
  }, []);

  const handleCalendarWeekTaskRefresh = (tasks) => {
    // Callback function called AFTER the week tasks have been refreshed
    var todayNumberOfTasks = 0;
    var todayCompletedTasks = 0;

    var todayTasks = tasks.filter((task) => {
      return isSameDay(new Date(task.taskStartDate), today);
    });

    todayNumberOfTasks = todayTasks.length;
    todayCompletedTasks = todayTasks.filter((task) => task.taskCompleted).length;

    setTodayNumberOfTasks(todayNumberOfTasks);
    setTodayCompletedTasks(todayCompletedTasks);
  };

  const onSettingsClose = () => {
    setLocalSettingsOpened(false);
  };

  const onUpdateSetting = (setting) => {
    if (setting.firstDisplayedHour) {
      //Prevents from displaying more than 24 hours
      if (settings.displayedHours > 24 - setting.firstDisplayedHour) {
        settings.displayedHours = 24 - setting.firstDisplayedHour;
      }
    }

    if (setting.displayedHours) {
      if (settings.firstDisplayedHour + setting.displayedHours > 24) {
        settings.firstDisplayedHour = 24 - setting.displayedHours;
      }
    }

    setSettings((prevSettings) => ({
      ...prevSettings,
      ...setting,
    }));
  };

  return (
    <Box>
      <Header />
      {areLocalSettingsOpened && (
        //Centered window on the screen
        <DOSettings onClose={onSettingsClose} onUpdateSetting={onUpdateSetting} settings={settings} />
      )}
      <Typography variant="h4" sx={{ my: 2, textAlign: 'center' }}>
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
        <WeekViewCalendar calendarRefreshCallback={handleCalendarWeekTaskRefresh} settings={settings} />
      </Box>

      <Grid2 container columns={12} spacing={2} sx={{ mt: 2 }} justifyContent="center" alignItems="center" direction="row">
        {/* Widgets */}
        <Grid2
          size={{ xs: 8, sm: 8, md: 4, lg: 3, xl: 3 }}
          sx={{
            backgroundColor: '#383838',
            aspectRatio: '1/1',
            borderRadius: 8,
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            p: 2,
          }}
        >
          {todayNumberOfTasks > 0 ? (
              <TasksCompletedGauge totalTasks={todayNumberOfTasks} completedTasks={todayCompletedTasks} />
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
