import { Box, Typography } from "@mui/material";

export default function OutsideHoursTask({ task}) {
    const borderColor = task.taskCompleted ? '#4caf50' : '#f44336';

  return (
    <Box
      key={task.taskId}
      sx={{
        width: '100%',
        borderRadius: '8px',
        position: 'relative',
        backgroundColor: '#f0f0f0',
        padding: '4px',
        maxHeight: '8px',
        overflow: 'hidden',
        border: `2px solid ${borderColor}`, // Use taskCompleted to determine border color

        //enlarge the box when the task is hovered, animate the height
        transition: 'max-height 0.1s ease-in-out',
        '&:hover': {
          maxHeight: '100px',
          overflow: 'visible',
        },
      }}
    >
      <Typography sx={{ color: 'black' }} variant="body2">
        {task.taskName}
      </Typography>
    </Box>
  );
}
