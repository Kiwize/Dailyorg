import { Box, Typography } from '@mui/material';
import GaugeComponent from 'react-gauge-component';

export default function TasksCompletedGauge({ totalTasks, completedTasks }) {
  // Calculate the percentage of completed tasks
  // Shows a gauge contained on an half circle
  return (
    <Box sx={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <Typography variant="h6" align="center" sx={{ mb: 2, textWrap: 'nowrap' }}>
        Tasks Completed ({completedTasks}/{totalTasks})
      </Typography>
      <GaugeComponent
        type="semicircle"
        minValue={0}
        maxValue={totalTasks}
        style={{
          width: '100%',
          height: '100%',
        }}
        arc={{
          colorArray: ['#FF2121', '#00FF15'],
          padding: 0.02,
          subArcs: [
            //Dynamically calculate subArcs depending on completed tasks and the number of total tasks
            ...Array(totalTasks)
              .fill(0)
              .map((_, index) => ({
                limit: index + 1,
              })),
          ],
        }}
        pointer={{ type: 'blob', animationDelay: 0 }}
        value={completedTasks}
      />
    </Box>
  );
}
