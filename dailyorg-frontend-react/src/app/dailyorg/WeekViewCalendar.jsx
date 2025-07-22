import React, { useEffect, useRef, useState } from 'react';
import { format, startOfWeek, addDays, addWeeks, subWeeks, isSameDay, parseISO, subDays, isSameMinute } from 'date-fns';
import { Box, Button, Typography, Paper, IconButton, InputLabel, MenuItem, Select, TextField, Checkbox } from '@mui/material';
import callApi from '../../hooks/api';
import CloseIcon from '@mui/icons-material/Close';
import { Task } from '@mui/icons-material';
import TaskCard from './TaskCard';

export default function WeekViewCalendar({ calendarRefreshCallback }) {
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [isAddFormShown, setIsAddFormShown] = useState(false);
  const [triggerRefresh, setTriggerRefresh] = useState(false);

  const [selectedTask, setSelectedTask] = useState(null);
  const [showcaseTaskPosition, setShowcaseTaskPosition] = useState({ x: 0, y: 0 });
  const [showcaseTaskSize, setShowcaseTaskSize] = useState({ width: 0, height: 0 });
  const isDragging = useRef(false);

  // State for error handling
  const [error, setError] = useState('');

  // State for form fields
  const [taskId, setTaskId] = React.useState('');
  const [taskName, setTaskName] = React.useState('');
  const [startTime, setStartTime] = React.useState('');
  const [endTime, setEndTime] = React.useState('');
  const [description, setDescription] = React.useState('');
  const [isCompleted, setIsCompleted] = React.useState(false);
  const [priority, setPriority] = React.useState('Low');
  const [energy, setEnergy] = React.useState('');

  const [isEditingTask, setIsEditingTask] = useState(false);

  const [currentDate, setCurrentDate] = useState(new Date());
  const startOfCurrentWeek = startOfWeek(currentDate, { weekStartsOn: 1 }); // Monday

  // Stores all the tasks for the selected week
  const [selectedWeekTasks, setSelectedWeekTasks] = useState([]);

  const weekDays = [...Array(7)].map((_, i) => {
    const date = addDays(startOfCurrentWeek, i);
    return {
      date,
      label: format(date, 'EEE dd'),
      isToday: isSameDay(date, new Date()),
    };
  });

  useEffect(() => {
    // Fetch tasks for the current week when the component mounts
    if (!localStorage.getItem('tasks.calendar.cache')) {
      localStorage.setItem('tasks.calendar.cache', JSON.stringify(new Map()));
    }

    retrieveTasksForWeek();

    //Calls the calendar refresh callback with the cached tasks for the current week
    calendarRefreshCallback(
      JSON.parse(localStorage.getItem('tasks.calendar.cache'))[getCacheKeyFromDate(startOfWeek(new Date(), { weekStartsOn: 1 })).split('T')[0]] || []
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
      setTaskId('');
      setTaskName('');
      setStartTime('');
      setEndTime('');
      setDescription('');
      setIsCompleted(false);
      setPriority('Low');
      setEnergy('');
      setError(''); // Reset error state
    }

    setTriggerRefresh(!triggerRefresh);
  };

  const handleDateSelect = (date) => {
    setSelectedDate(date);
    console.log('Selected date:', date);
  };

  const handleTaskClick = (task) => {
    // Handle task click event
    setIsEditingTask(true);
    handleAddTask(true);

    setTaskId(task.taskId);
    setTaskName(task.taskName);
    setStartTime(task.taskStartDate);
    setEndTime(task.taskEndDate);
    setDescription(task.taskDescription);
    setIsCompleted(task.taskCompleted);
    setPriority(task.taskPriority.taskPriorityName);
    setEnergy(task.taskRequiredEnergy);

    setTriggerRefresh(!triggerRefresh);
  };

  const handleDeleteTask = async (taskId) => {
    // Deletes a task by its ID
    await callApi(
      'DELETE',
      'task/delete_task',
      {
        task_id: taskId,
        user_email: localStorage.getItem('username'),
      },
      {},
      true,
      false
    );

    retrieveTasksForWeek(true);
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

    try {
      const response = await callApi(
        'PUT',
        `task/${isEditingTask ? 'update_task' : 'create_task'}`,
        {
          task_id: isEditingTask ? taskId : null, // Only include task_id if editing
          is_task_completed: isCompleted,
          task_name: taskName,
          user_email: localStorage.getItem('username'),
          task_start_date: startTime,
          task_end_date: endTime,
          task_description: description,
          task_required_energy: parseInt(energy, 10),
          task_priority: priority,
        },
        {},
        true,
        false
      );

      handleAddTask(false); // Hide the form after successful submission
    } catch (err) {
      setError(err.message);
    }

    retrieveTasksForWeek(true);
  };

  const retrieveTasksForWeek = async (forceRefresh = false) => {
    // Fetch tasks for the current week from the backend
    var taskData = [];
    var currentWeekKey = getCacheKeyFromDate(startOfCurrentWeek);

    const cache = JSON.parse(localStorage.getItem('tasks.calendar.cache'));

    if (cache[currentWeekKey] && !forceRefresh) {
      taskData = cache[currentWeekKey];
    } else {
      const response = await callApi('POST', 'task/get_tasks_between_dates', {
        user_email: localStorage.getItem('username'),
        start_date: startOfCurrentWeek.toLocaleString('sv-SE').replace(' ', 'T'),
        end_date: addDays(startOfCurrentWeek, 7).toLocaleString('sv-SE').replace(' ', 'T'),
      });

      // Store the tasks in the local cache
      cache[currentWeekKey] = response;
      localStorage.setItem('tasks.calendar.cache', JSON.stringify(cache));
      taskData = response;
    }

    setSelectedWeekTasks(
      taskData.map((task) => ({
        ...task,
        start: parseISO(task.taskStartDate),
        end: parseISO(task.taskEndDate),
      }))
    );
  };

  // Helper to get top and height percentages for a task
  function getTaskPosition(start, end) {
    const startHour = start.getHours() + start.getMinutes() / 60;
    const endHour = end.getHours() + end.getMinutes() / 60;
    const top = (startHour / 24) * 100;
    const height = ((endHour - startHour) / 24) * 100;
    return { top: `${top}%`, height: `${height}%` };
  }

  function updateTaskBeforeDrag(task) {
    // Prepare the task for dragging by converting start and end dates to Date objects
    setSelectedTask(task);
    isDragging.current = true;
  }

  function updateTaskWhileDrag(task, x, y, target) {
    //update the values in the selectedWeekTasks state
    setSelectedWeekTasks((prevTasks) => prevTasks.map((t) => (t.taskId === task.taskId ? { ...t, start: task.start, end: task.end } : t)));

    setShowcaseTaskPosition({ x, y });

    console.log('New position:', target.style);
    setShowcaseTaskSize({ width: target.offsetWidth, height: target.offsetHeight });

    setTriggerRefresh(!triggerRefresh);
  }

  function updateTaskAfterDrag(task, newStartDate, newEndDate) {
    // Update the task position in the backend
    callApi(
      'PUT',
      'task/update_task',
      {
        task_id: task.taskId,
        is_task_completed: task.isCompleted,
        task_name: task.taskName,
        user_email: localStorage.getItem('username'),
        task_start_date: newStartDate.toLocaleString('sv-SE').replace(' ', 'T'),
        task_end_date: newEndDate.toLocaleString('sv-SE').replace(' ', 'T'),
        task_description: task.taskDescription,
        task_required_energy: parseInt(task.taskRequiredEnergy, 10),
        task_priority: task.taskPriority.taskPriorityName,
      },
      {},
      true,
      false
    )
      .then(() => {
        // Refresh the tasks after updating
        retrieveTasksForWeek(true);
        setTriggerRefresh(!triggerRefresh);
      })
      .catch((error) => {
        console.error('Error updating task:', error);
        setError('Failed to update task. Please try again.');
      });

    isDragging.current = false; // Reset dragging state
    setSelectedTask(null); // Clear selected task after dragging
  }

  return (
    <Box className="h-full" sx={{ margin: '0 auto', p: 2 }}>
      {isAddFormShown && (
        <Box
          sx={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
            zIndex: 1000,
          }}
        >
          <Box
            sx={{
              position: 'absolute',
              top: '50%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
              backgroundColor: 'background.paper',
              padding: 4,
              borderRadius: 2,
              boxShadow: 3,
            }}
          >
            <IconButton variant="contained" color="error" onClick={() => handleAddTask(false)} sx={{ position: 'absolute', top: 8, right: 8 }}>
              <CloseIcon />
            </IconButton>
            <Typography variant="h5" sx={{ mb: 5, textAlign: 'center' }}>
              {isEditingTask ? 'Edit Task' : 'Add Task'}
            </Typography>
            {/* Add your form fields here */}
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <form
                onSubmit={(e) => {
                  handleAddTaskFormSubmit(e);
                }}
                style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}
              >
                <TextField type="text" placeholder="Task Name" value={taskName} onChange={(event) => setTaskName(event.target.value)} required />

                <Box sx={{ display: 'flex', flexDirection: 'row', gap: 2 }}>
                  <InputLabel>Start time :</InputLabel>
                  <input
                    type="datetime-local"
                    placeholder="Start Time"
                    value={startTime}
                    name="start_time"
                    onChange={(event) => setStartTime(event.target.value)}
                    required
                  />
                </Box>
                <Box sx={{ display: 'flex', flexDirection: 'row', gap: 2 }}>
                  <InputLabel>End time :</InputLabel>
                  <input
                    type="datetime-local"
                    value={endTime}
                    placeholder="End Time"
                    name="end_time"
                    onChange={(event) => setEndTime(event.target.value)}
                    required
                  />
                </Box>
                <textarea placeholder="Description" value={description} rows={4} onChange={(event) => setDescription(event.target.value)}></textarea>

                <TextField
                  type="number"
                  placeholder="Energy Required (1-10)"
                  min={1}
                  max={10}
                  value={energy}
                  onChange={(event) => setEnergy(event.target.value)}
                  required
                />

                <Select labelId="Priority" value={priority} name="priority" onChange={(event) => setPriority(event.target.value)} required>
                  <MenuItem value="Low">Low</MenuItem>
                  <MenuItem value="Medium">Medium</MenuItem>
                  <MenuItem value="High">High</MenuItem>
                </Select>

                {isEditingTask && (
                  <Box sx={{ display: 'flex', flexDirection: 'row', gap: 2 }}>
                    <InputLabel>Is completed :</InputLabel>
                    <Checkbox checked={isCompleted} value={isCompleted} onChange={(event) => setIsCompleted(event.target.checked)} />
                  </Box>
                )}

                <Button variant="contained" color="success" type="submit" sx={{ mt: 2 }}>
                  {isEditingTask ? 'Update Task' : 'Add Task'}
                </Button>

                {isEditingTask && (
                  <Button
                    variant="outlined"
                    color="error"
                    onClick={() => {
                      setIsEditingTask(false);
                      handleAddTask(false);
                      handleDeleteTask(taskId);
                    }}
                    sx={{ mt: 2 }}
                  >
                    Delete Task
                  </Button>
                )}

                {error && <Typography color="error">{error}</Typography>}
              </form>
            </Box>
          </Box>
        </Box>
      )}
      <Typography variant="h4" align="center" gutterBottom>
        {format(currentDate, 'MMMM yyyy dd')} Week View
      </Typography>
      {selectedTask && (
        <Box sx=
        {{ 
          position: 'absolute', 
          top: showcaseTaskPosition.y, 
          left: showcaseTaskPosition.x, 
          zIndex: 1000,
          width: showcaseTaskSize.width || 'auto',
          height: showcaseTaskSize.height || 'auto',
        }}>
          <TaskCard
            task={selectedTask}
            updateTaskAfterDrag={updateTaskAfterDrag}
            updateTaskWhileDrag={updateTaskWhileDrag}
            getTaskPosition={getTaskPosition}
            handleTaskClick={handleTaskClick}
            updateTaskBeforeDrag={updateTaskBeforeDrag}
            onClick={() => {}}
          />
        </Box>
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
        gridTemplateColumns="repeat(7, 1fr)"
        gap={1}
        sx={{
          height: '100%',
          minHeight: { md: '300px' },
        }}
      >
        {weekDays.map(({ date, label, isToday }) => (
          <Paper
            id={`date-${date.toLocaleString('sv-SE').split(' ')[0]}`}
            key={date.toISOString()}
            elevation={isToday ? 4 : 1}
            sx={{
              position: 'relative',
              padding: 2,
              backgroundColor: isToday ? 'primary.dark' : 'background.paper',
              border: isToday ? '2px solid' : '1px solid',
              borderColor: isToday ? 'primary.main' : 'grey.300',
              textAlign: 'center',
              height: { xs: 'auto', md: '40vh' },
              minHeight: { md: '40%' },
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              overflow: 'hidden',
            }}
            //onClick={() => handleDateSelect(date)}
          >
            <Typography variant="body1" style={{ userSelect: 'none' }}>
              {label}
            </Typography>
            {/* Render the task only if it matches this date */}
            {selectedWeekTasks &&
              selectedWeekTasks.map((task) => {
                return (
                  isSameDay(date, task.start) && (
                    <TaskCard
                      key={task.taskId}
                      task={task}
                      updateTaskAfterDrag={updateTaskAfterDrag}
                      updateTaskWhileDrag={updateTaskWhileDrag}
                      getTaskPosition={getTaskPosition}
                      handleTaskClick={handleTaskClick}
                      updateTaskBeforeDrag={updateTaskBeforeDrag}
                      onClick={() => {
                        setSelectedTask(task);
                      }}
                    />
                  )
                );
              })}
          </Paper>
        ))}
      </Box>
      <Box sx={{ textAlign: 'center' }}>
        <Button variant="contained" color="success" onClick={() => handleAddTask(true)}>
          Add Task
        </Button>
      </Box>
    </Box>
  );
}
