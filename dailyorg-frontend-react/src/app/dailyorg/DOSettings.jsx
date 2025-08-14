import { Box, Button } from '@mui/material';

export default function DOSettings({ onClose, onUpdateSetting, settings }) {
  return (
    <Box
      sx={{
        position: 'fixed',
        top: 0,
        left: 0,
        zIndex: 1000,
        width: '100%',
        height: '100%',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}
    >
      <Box
        sx={{
          position: 'fixed',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          minWidth: '300px',
          minHeight: '200px',
          backgroundColor: 'background.paper',
          boxShadow: 24,
          p: 4,
          zIndex: 2000,
        }}
      >
        <Button variant="contained" onClick={() => onClose()} sx={{ mb: 2 }}>
          Close Settings
        </Button>
        <Box>
          <h2>Settings</h2>
          <Box>
            <label>
              First Displayed Hour:
              <input type="number" max={23} min={0} value={settings.firstDisplayedHour} onChange={(e) => onUpdateSetting({ firstDisplayedHour: e.target.value })} />
            </label>
          </Box>
          <Box>
            <label>
              Displayed Hours:
              <input type="number" max={24} min={1} value={settings.displayedHours} onChange={(e) => onUpdateSetting({ displayedHours: e.target.value })} />
            </label>
          </Box>
        </Box>
      </Box>
    </Box>
  );
}
