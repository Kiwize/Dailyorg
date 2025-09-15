import React, { useEffect, useRef, useState } from 'react';
import { format, startOfWeek, addDays, addWeeks, subWeeks, isSameDay, parseISO, subDays, isSameMinute, set, startOfToday, getHours } from 'date-fns';
import { Box, Button, Typography, Paper } from '@mui/material';
import callApi from '../../hooks/api';

import TaskCard from './TaskCard';
import useAlert from '../../hooks/useAlert';
import VisualTimeIndicator from './VisualTimeIndicator';
import OutsideHoursTask from './OutsideHoursTask';
import TaskAddUpdateForm from './TaskAddUpdateForm';
import useLoading from '../../hooks/useLoading';
import LoadingScreen from '../../components/LoadingScreen';

export default function WeekViewCalendar({ calendarRefreshCallback, settings, triggerRefresh }) {
  const [isAddFormShown, setIsAddFormShown] = useState(false);
  const alert = useAlert();
  const loading = useLoading();

  const setTriggerRefresh = (value) => {
    triggerRefresh = value;
  }

  const [selectedTask, setSelectedTask] = useState(null);

  const [currentDate, setCurrentDate] = useState(new Date());
  const startOfCurrentWeek = startOfWeek(currentDate, { weekStartsOn: 1 }); // Monday

  // State for form fields
  const [taskData, setTaskData] = React.useState({
    id: '',
    category: null,
    taskName: '',
    startTime: '',
    endTime: '',
    description: '',
    isCompleted: false,
    priority: 'Low',
    energy: 1,
    wasTaskMarkedDone: false,
    is_recurrent: false,
    repeatFrequency: null,
    repeatEndDate: '',
  });

  const [isEditingTask, setIsEditingTask] = useState(false);

  // Stores all the tasks for the selected week
  const [selectedWeekTasks, setSelectedWeekTasks] = useState([]);
  const [visualTimeIndicators, setVisualTimeIndicators] = useState([]);

  //Lists of tasks displayed outside of the displayed hours
  //Those tasks can be before or after the displayed hours
  const [beforeDisplayedTasks, setBeforeDisplayedTasks] = useState([]);
  const [afterDisplayedTasks, setAfterDisplayedTasks] = useState([]);

  const weekDays = [...Array(7)].map((_, i) => {
    const date = addDays(startOfCurrentWeek, i);
    return {
      date,
      label: format(date, 'EEE dd'),
      isToday: isSameDay(date, new Date()),
    };
  });

  useEffect(() => {
    retrieveTasksForWeek(false, false);
  }, [settings, triggerRefresh]);

  useEffect(() => {
    // Fetch tasks for the current week when the component mounts
    if (!localStorage.getItem('tasks.calendar.cache_' + localStorage.getItem('username'))) {
      //Delete previous cache if it exists
      for (let i = localStorage.length - 1; i >= 0; i--) {
        const key = localStorage.key(i);
        if (key.startsWith('tasks.calendar.cache_')) {
          localStorage.removeItem(key);
        }
      }

      localStorage.setItem('tasks.calendar.cache_' + localStorage.getItem('username'), JSON.stringify(new Map()));
    }

    retrieveTasksForWeek(true);

    //Calls the calendar refresh callback with the cached tasks for the current week
    calendarRefreshCallback(
      JSON.parse(localStorage.getItem('tasks.calendar.cache_' + localStorage.getItem('username')))[
        getCacheKeyFromDate(startOfWeek(new Date(), { weekStartsOn: 1 })).split('T')[0]
      ] || []
    );
  }, [currentDate, triggerRefresh]);

  const goToPreviousWeek = () => setCurrentDate(subWeeks(currentDate, 1));
  const goToNextWeek = () => setCurrentDate(addWeeks(currentDate, 1));

  const handleAddTask = (newState) => {
    //Displays a form over the current page to add a task
    //There are multiple fields, task name, start time, end time, description, energy required, and priority
    setIsAddFormShown(newState);
    if (!newState) {
      setIsEditingTask(false);
    }

    // Reset form fields when hiding the form
    if (!newState) {
      setTaskData({
        id: '',
        category: null,
        taskName: '',
        startTime: '',
        endTime: '',
        description: '',
        isCompleted: false,
        priority: 'Low',
        energy: 1,
        wasTaskMarkedDone: false,
        is_recurrent: false,
        repeatFrequency: null,
        repeatEndDate: '',
      });
    }

    setTriggerRefresh(!triggerRefresh);
  };

  const handleTaskClick = (task) => {
    // Handle task click event
    setIsEditingTask(true);
    handleAddTask(true);

    setTaskData({
      id: task.id,
      category: task.category ? task.category.idCategory : null,
      taskName: task.taskName,
      startTime: task.taskStartDate,
      endTime: task.taskEndDate,
      description: task.taskDescription,
      isCompleted: task.taskCompleted,
      priority: task.taskPriority.taskPriorityName,
      energy: task.taskRequiredEnergy,
      wasTaskMarkedDone: task.taskCompleted,
      isRecurrent: task.recurringTaskState !== null,
      repeatFrequency: task.recurringTaskState ? task.recurringTaskState.recurringTaskStateId : null,
      repeatEndDate: task.recurringTaskState ? task.recurrenceEndDate.split('T')[0] : null,
    });

    setTriggerRefresh(!triggerRefresh);
  };

  const handleDeleteTask = async (id) => {
    // Deletes a task by its ID
    await callApi(
      'DELETE',
      'task/delete_task',
      {
        task_id: id,
        user_email: localStorage.getItem('username'),
      },
      {},
      true,
      false
    );

    retrieveTasksForWeek(true);
    alert.setAlert('Task deleted successfully', 'success');
    setSelectedTask(null);
    setTriggerRefresh(!triggerRefresh);
  };

  const getCacheKeyFromDate = (date) => {
    // Generates a cache key based on the date
    var result = `${subDays(date, 0).toLocaleString('sv-SE').replace(' ', 'T').split('T')[0]}-${
      addDays(date, 6).toLocaleString('sv-SE').replace(' ', 'T').split('T')[0]
    }`;
    return result;
  };

  const handleAddTaskFormSubmit = async (event) => {
    event.preventDefault();

    if (taskData.isRecurrent && taskData.repeatEndDate == null) {
      alert.setAlert('Please provide both repeat frequency and end date for recurring tasks.', 'error');
      return;
    }

    try {
      await callApi(
        'PUT',
        `task/${isEditingTask ? 'update_task' : 'create_task'}`,
        {
          task_id: isEditingTask ? taskData.id : null, // Only include task_id if editing
          task_category_id: taskData.category ? taskData.category : null,
          is_task_completed: taskData.isCompleted,
          task_name: taskData.taskName,
          task_start_date: taskData.startTime,
          task_end_date: taskData.endTime,
          task_description: taskData.description,
          task_required_energy: parseInt(taskData.energy, 10),
          task_priority: taskData.priority,
          is_recurrent: taskData.isRecurrent,
          task_repeat_frequency: taskData.isRecurrent ? taskData.repeatFrequency : null,
          task_repeat_end_date: taskData.isRecurrent ? taskData.repeatEndDate : null,
        },
        {},
        true,
        false
      ).then(() => {
        // Refresh the tasks after adding or updating
        retrieveTasksForWeek(true);
      });

      handleAddTask(false); // Hide the form after successful submission
      alert.setAlert(`Task ${isEditingTask ? 'updated' : 'added'} successfully`, 'success');
    } catch (err) {
      alert.setAlert(`Failed to ${isEditingTask ? 'update' : 'add'} task`, 'error');
    }
  };

  const retrieveTasksForWeek = async (forceRefresh = false, showLoadingScreen = true) => {
    loading.setIsLoading(showLoadingScreen);

    // Fetch tasks for the current week from the backend
    var taskData = [];
    var currentWeekKey = getCacheKeyFromDate(startOfCurrentWeek);

    var cache = JSON.parse(localStorage.getItem('tasks.calendar.cache_' + localStorage.getItem('username')));

    if (!cache) {
      localStorage.setItem('tasks.calendar.cache_' + localStorage.getItem('username'), JSON.stringify({}));
      cache = JSON.parse(localStorage.getItem('tasks.calendar.cache_' + localStorage.getItem('username')));
    }

    if (cache[currentWeekKey] && !forceRefresh) {
      taskData = cache[currentWeekKey];
    } else {
      const response = await callApi('POST', 'task/get_tasks_between_dates', {
        start_date: startOfCurrentWeek.toLocaleString('sv-SE').replace(' ', 'T'),
        end_date: addDays(startOfCurrentWeek, 7).toLocaleString('sv-SE').replace(' ', 'T'),
      });

      // Store the tasks in the local cache
      cache[currentWeekKey] = response.content;
      localStorage.setItem('tasks.calendar.cache_' + localStorage.getItem('username'), JSON.stringify(cache));
      taskData = response.content;
    }

    //Update the task data according to the selected task's new start and end dates
    //find the task with the same ID as the selected task and update its start and end dates

    if (selectedTask) {
      taskData = taskData.map((task) => {
        if (task.id === selectedTask.id) {
          return {
            ...task,
            taskStartDate: selectedTask.taskStartDate,
            taskEndDate: selectedTask.taskEndDate,
          };
        }
        return task;
      });
    }

    setSelectedWeekTasks(
      taskData.map((task) => ({
        ...task,
        wasTaskMarkedDone: task.taskCompleted,
        start: parseISO(task.taskStartDate),
        end: parseISO(task.taskEndDate),
      }))
    );

    const beforeTasks = taskData.filter((task) => {
      const taskEnd = parseISO(task.taskEndDate);
      return getHours(taskEnd) <= Number(settings.firstDisplayedHour);
    });

    const afterTasks = taskData.filter((task) => {
      const taskEnd = parseISO(task.taskStartDate);
      return getHours(taskEnd) >= Number(settings.firstDisplayedHour) + Number(settings.displayedHours);
    });

    setBeforeDisplayedTasks(beforeTasks);
    setAfterDisplayedTasks(afterTasks);

    // Generate visual time indicators for the displayed hours
    //Add indicators for each quarter hours IF there are 8 or less displayed hours
    const visualIndicators = [];

    for (let i = 0; i < settings.displayedHours; i++) {
      const baseDate = startOfToday(); // Midnight today (00:00)
      const time = set(baseDate, {
        hours: Number(settings.firstDisplayedHour) + i,
        minutes: 0,
        seconds: 0,
        milliseconds: 0,
      });

      visualIndicators.push(time);

      loading.setIsLoading(false);
    }

    if (settings.displayedHours <= 8) {
      for (let i = 0; i < settings.displayedHours * 4; i++) {
        const time = set(startOfToday(), { hours: settings.firstDisplayedHour + Math.floor(i / 4), minutes: (i % 4) * 15, seconds: 0 });
        visualIndicators.push(time);
      }
    }

    setVisualTimeIndicators(visualIndicators);
  };

  function updateTaskBeforeDrag(task, target) {
    // Prepare the task for dragging by converting start and end dates to Date objects
    setSelectedTask(task);
  }

  function updateTaskWhileDrag(task, x, y, target) {
    //update the values in the selectedWeekTasks state
    setSelectedWeekTasks((prevTasks) => prevTasks.map((t) => (t.id === task.id ? { ...t, start: task.start, end: task.end } : t)));

    selectedTask.taskStartDate = x.toLocaleString('sv-SE').replace(' ', 'T');
    selectedTask.taskEndDate = y.toLocaleString('sv-SE').replace(' ', 'T');

    retrieveTasksForWeek(false, false);
    setTriggerRefresh(!triggerRefresh);
  }

  function updateTaskAfterDrag(task, newStartDate, newEndDate) {
      console.log('Updating task after drag:', task, newStartDate, newEndDate);

    // Update the task position in the backend
    callApi(
      'PUT',
      'task/update_task',
      {
        task_id: task.id,
        task_category_id: task.category ? task.category.idCategory : null,
        is_task_completed: task.isCompleted,
        task_name: task.taskName,
        user_email: localStorage.getItem('username'),
        task_start_date: newStartDate.toLocaleString('sv-SE').replace(' ', 'T'),
        task_end_date: newEndDate.toLocaleString('sv-SE').replace(' ', 'T'),
        task_description: task.taskDescription,
        task_required_energy: parseInt(task.taskRequiredEnergy, 10),
        is_recurrent: task.recurringTaskState !== null,
        task_repeat_frequency: task.recurringTaskState ? task.recurringTaskState.recurringTaskStateId : null,
        task_repeat_end_date: task.recurrenceEndDate ? task.recurrenceEndDate.split('T')[0] : null,
        task_priority: task.taskPriority.taskPriorityName,
      },
      {},
      true,
      false
    )
      .then(() => {
        // Refresh the tasks after updating
        retrieveTasksForWeek(true, false);
        setTriggerRefresh(!triggerRefresh);
      })
      .catch((error) => {
        alert.setAlert('Failed to update task. Please try again.', 'error');
      });

    setSelectedTask(null); // Clear selected task after dragging
  }

  return (
    <Box className="h-full" id="week-view-calendar" sx={{ margin: '0 auto', p: 2, width: { xs: '100%', md: '90%' }, overflow: 'hidden' }}>
      {isAddFormShown && (
        <TaskAddUpdateForm
          handleAddTask={handleAddTask}
          handleDeleteTask={handleDeleteTask}
          isEditingTask={isEditingTask}
          taskData={taskData}
          setTaskData={setTaskData}
          handleAddTaskFormSubmit={handleAddTaskFormSubmit}
          setIsEditingTask={setIsEditingTask}
        />
      )}
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
        <Button variant="outlined" onClick={goToPreviousWeek}>
          ‹ Prev
        </Button>
        <Typography variant="h6">{format(startOfCurrentWeek, 'MMMM yyyy')}</Typography>
        <Button variant="outlined" onClick={goToNextWeek}>
          Next ›
        </Button>
      </Box>
      <Box
        className="grid h-full"
        sx={{ maxWidth: '100vw', overflowX: 'auto' }}
        gridTemplateColumns={{ xs: 'repeat(7, 120px)', md: 'repeat(7, 1fr)' }}
        gap={1}
      >
        {weekDays.map(({ date, label, isToday }) => (
          <Box key={date.toISOString() + '-box'}>
            <Typography
              variant="subtitle1"
              sx={{ textAlign: 'center', textWrap: 'nowrap', fontWeight: 'bold', color: isToday ? 'primary.main' : 'text.primary' }}
            >
              {label}
            </Typography>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'space-evenly',
                paddingBottom: 2,
                minHeight: beforeDisplayedTasks.length * 42 + 'px', // Adjust height based on the number of tasks
              }}
            >
              {beforeDisplayedTasks.map((task) => {
                return isSameDay(date, task.taskStartDate) && <OutsideHoursTask key={task.id + '_before'} task={task} />;
              })}
            </Box>
            <Paper
              id={`date-${date.toLocaleString('sv-SE').split(' ')[0]}`}
              key={date.toISOString()}
              elevation={isToday ? 4 : 1}
              sx={{
                position: 'relative',
                padding: 2,
                backgroundColor: isToday ? '#002699' : 'background.paper',
                border: isToday ? '2px solid' : '1px solid',
                borderColor: isToday ? 'primary.main' : 'grey.300',
                textAlign: 'center',
                height: { xs: '70vh', md: '70vh' },
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                overflow: 'hidden',
              }}
              //onClick={() => handleDateSelect(date)}
            >
              {/* Render the task only if it matches this date */}
              {selectedWeekTasks &&
                selectedWeekTasks.map((task) => {
                  return (
                    isSameDay(date, task.start) && (
                      <TaskCard
                        key={task.id}
                        task={task}
                        isTaskSelected={selectedTask && selectedTask.id === task.id}
                        updateTaskAfterDrag={updateTaskAfterDrag}
                        updateTaskWhileDrag={updateTaskWhileDrag}
                        handleTaskClick={handleTaskClick}
                        updateTaskBeforeDrag={updateTaskBeforeDrag}
                        displayConfig={{ displayedHours: settings.displayedHours, firstDisplayedHour: settings.firstDisplayedHour }}
                      />
                    )
                  );
                })}
              {/* Render visual time indicators for each hour in the displayed range */}
              {visualTimeIndicators &&
                visualTimeIndicators.map((time) => (
                  <VisualTimeIndicator
                    key={time.toISOString() + Math.random()}
                    time={time}
                    firstDisplayedHour={settings.firstDisplayedHour}
                    displayedHours={settings.displayedHours}
                  />
                ))}
            </Paper>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'space-evenly',
                paddingTop: 2,
                minHeight: afterDisplayedTasks.length * 64 + 'px', // Adjust height based on the number of tasks
              }}
            >
              {afterDisplayedTasks.map((task) => {
                return isSameDay(date, task.taskStartDate) && <OutsideHoursTask key={task.id + '_after'} task={task} />;
              })}
            </Box>
          </Box>
        ))}
      </Box>
      <Box sx={{ textAlign: 'center', mt: 2 }}>
        <Button variant="contained" color="success" onClick={() => handleAddTask(true)}>
          Add Task
        </Button>
      </Box>
    </Box>
  );
}
