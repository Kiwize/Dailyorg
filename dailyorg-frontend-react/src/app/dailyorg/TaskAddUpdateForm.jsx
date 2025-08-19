import { Box, Typography, IconButton, InputLabel, MenuItem, Select, TextField, Checkbox, Button } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { useEffect, useState } from 'react';
import callApi from '../../hooks/api';
import useWindowSize from '../../hooks/useWindowSize';
import Divider from '../../components/Divider';

export default function TaskAddUpdateForm({
  handleAddTask,
  handleDeleteTask,
  isEditingTask,
  taskData,
  setTaskData,
  handleAddTaskFormSubmit,
  setIsEditingTask,
}) {
  const { width } = useWindowSize();

  const [showTaskOccurrenceConfig, setShowTaskOccurrenceConfig] = useState(false);
  const [taskRepeatFrequencies, setTaskRepeatFrequencies] = useState([]);

  useEffect(() => {
    // Initialize task repeat frequencies if needed
    const response = callApi('GET', 'task/get_all_tasks_recurring_states');
    response.then((data) => {
      if (data.status === 200) {
        //Filter the data to exclude the same display_name
        // IF the task already refers a frequency, add that frequency to the list

        setTaskRepeatFrequencies(data.content);
        if (taskData.isRecurrent) {
          setShowTaskOccurrenceConfig(true);
        }
      }
    });
  }, []);

  useEffect(() => {
    console.log(taskRepeatFrequencies);
    if (taskRepeatFrequencies.length > 0 && !taskData.repeatFrequency) {
      // Set default repeat frequency if not already set
      setTaskData({ ...taskData, repeatFrequency: taskRepeatFrequencies[0].id });
    }
  }, [taskRepeatFrequencies]);

  useEffect(() => {
    if (showTaskOccurrenceConfig) {
      setTaskData({ ...taskData, isRecurrent: true });
    } else {
      setTaskData({ ...taskData, isRecurrent: false });
    }
  }, [showTaskOccurrenceConfig]);

  return (
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
          transform: `${width >= 800 && showTaskOccurrenceConfig ? 'translate(-102%, -50%)' : 'translate(-50%, -50%)'}`,
          backgroundColor: 'background.paper',
          padding: 4,
          borderRadius: 2,
          boxShadow: 3,
          maxHeight: '90vh',
          overflowY: 'auto',
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
            <TextField
              type="text"
              placeholder="Task Name"
              value={taskData.taskName}
              onChange={(event) => setTaskData({ ...taskData, taskName: event.target.value })}
              required
            />

            <Box sx={{ display: 'flex', flexDirection: 'row', gap: 2 }}>
              <InputLabel>Start time :</InputLabel>
              <input
                type="datetime-local"
                placeholder="Start Time"
                value={taskData.startTime}
                name="start_time"
                onChange={(event) => setTaskData({ ...taskData, startTime: event.target.value })}
                required
              />
            </Box>
            <Box sx={{ display: 'flex', flexDirection: 'row', gap: 2 }}>
              <InputLabel>End time :</InputLabel>
              <input
                type="datetime-local"
                value={taskData.endTime}
                placeholder="End Time"
                name="end_time"
                onChange={(event) => setTaskData({ ...taskData, endTime: event.target.value })}
                required
              />
            </Box>
            <Button
              variant="outlined"
              color={showTaskOccurrenceConfig ? 'success' : 'error'}
              onClick={() => {
                setShowTaskOccurrenceConfig(!showTaskOccurrenceConfig);
              }}
              sx={{ mt: 2 }}
            >
              Repeat Task : {showTaskOccurrenceConfig ? 'Enabled' : 'Disabled'}
            </Button>
            {width <= 800 && showTaskOccurrenceConfig && (
              <Box>
                <Divider />
                <Typography variant="h6">Task Occurrence Configuration</Typography>
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
                  <InputLabel>Repeat Frequency</InputLabel>
                  <Select
                    labelId="repeat-frequency"
                    value={taskData.repeatFrequency}
                    onChange={(event) => {
                      setTaskData({ ...taskData, repeatFrequency: event.target.value });
                    }}
                  >
                    {taskRepeatFrequencies.map((frequency) => (
                      <MenuItem key={frequency.id} value={frequency.id}>
                        {frequency.display_name}
                      </MenuItem>
                    ))}
                  </Select>
                </Box>
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
                  <InputLabel>End Date</InputLabel>
                  <input
                    type="date"
                    value={taskData.repeatEndDate}
                    onChange={(event) => setTaskData({ ...taskData, repeatEndDate: event.target.value })}
                  />
                </Box>
                <Divider/>
              </Box>
            )}

            <textarea
              placeholder="Description"
              value={taskData.description}
              rows={4}
              onChange={(event) => setTaskData({ ...taskData, description: event.target.value })}
            ></textarea>

            <TextField
              type="number"
              placeholder="Energy Required (1-10)"
              min={1}
              max={10}
              disabled={taskData.wasTaskMarkedDone}
              value={taskData.energy}
              onChange={(event) => setTaskData({ ...taskData, energy: event.target.value })}
              required
            />

            <Select
              labelId="Priority"
              value={taskData.priority}
              name="priority"
              disabled={taskData.wasTaskMarkedDone}
              onChange={(event) => setTaskData({ ...taskData, priority: event.target.value })}
              required
            >
              <MenuItem value="Low">Low</MenuItem>
              <MenuItem value="Medium">Medium</MenuItem>
              <MenuItem value="High">High</MenuItem>
            </Select>

            {isEditingTask && (
              <Box sx={{ display: 'flex', flexDirection: 'row', gap: 2 }}>
                <InputLabel>Is completed :</InputLabel>
                <Checkbox
                  checked={taskData.isCompleted}
                  value={taskData.isCompleted}
                  onChange={(event) => setTaskData({ ...taskData, isCompleted: event.target.checked })}
                />
              </Box>
            )}
            {(!showTaskOccurrenceConfig || width <= 800) && (
              <>
                <Button variant="contained" color="success" type="submit">
                  {isEditingTask ? 'Update Task' : 'Add Task'}
                </Button>
                {isEditingTask && (
                  <Button
                    variant="outlined"
                    color="error"
                    onClick={() => {
                      setIsEditingTask(false);
                      handleAddTask(false);
                      handleDeleteTask(taskData.id);
                    }}
                  >
                    Delete Task
                  </Button>
                )}
              </>
            )}
          </form>
        </Box>
      </Box>
      {width > 800 && showTaskOccurrenceConfig && (
        <Box
          sx={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(2%, -50%)',
            backgroundColor: 'background.paper',
            padding: 4,
            borderRadius: 2,
            boxShadow: 3,
          }}
        >
          <Typography variant="h6">Task Occurrence Configuration</Typography>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
            <InputLabel>Repeat Frequency</InputLabel>
            <Select
              labelId="repeat-frequency"
              value={taskData.repeatFrequency}
              onChange={(event) => {
                setTaskData({ ...taskData, repeatFrequency: event.target.value });
              }}
            >
              {taskRepeatFrequencies.map((frequency) => (
                <MenuItem key={frequency.id} value={frequency.id}>
                  {frequency.display_name}
                </MenuItem>
              ))}
            </Select>
          </Box>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
            <InputLabel>End Date</InputLabel>
            <input type="date" value={taskData.repeatEndDate} onChange={(event) => setTaskData({ ...taskData, repeatEndDate: event.target.value })} />
          </Box>
          {showTaskOccurrenceConfig && (
            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
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
                    handleDeleteTask(taskData.id);
                  }}
                  sx={{ mt: 2 }}
                >
                  Delete Task
                </Button>
              )}
            </Box>
          )}
        </Box>
      )}
    </Box>
  );
}
