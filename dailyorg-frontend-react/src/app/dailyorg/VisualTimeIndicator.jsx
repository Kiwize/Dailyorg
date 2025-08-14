import { BorderTop } from '@mui/icons-material';
import { Typography } from '@mui/material';

export default function VisualTimeIndicator({ time, firstDisplayedHour, displayedHours }) {
  //Represents an horizontal line indicating each hours on the calendar
  //The height is calculated depending on the time
  const isQuarterHour = time.getMinutes() !== 0;

  const getTaskPosition = (hour) => {
    const startHour = hour.getHours() + hour.getMinutes() / 60;

    const top = ((startHour - firstDisplayedHour) / displayedHours) * 100;

    return { top: `${top}%` };
  };

  //Indicators for quarter hours are less visible, so we use a lighter color
  return (
    <div
      className="visual-time-indicator"
      style={{
        position: 'absolute',
        left: 0,
        width: '100%',
        ...getTaskPosition(time),
        borderTop: `${isQuarterHour ? '1px dashed #b3c6ff' : '2px solid #b3c6ff'} `,
      }}
    >
      {!isQuarterHour && (
        <Typography variant="caption" sx={{ position: 'absolute', left: 10, top: '-20px', color: '#b3c6ff' }}>
          {time.toLocaleTimeString([], {
            hour: '2-digit',
            minute: '2-digit',
          })}
        </Typography>
      )}
    </div>
  );
}

