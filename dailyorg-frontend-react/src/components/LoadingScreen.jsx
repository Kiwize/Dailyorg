import { Box, Typography } from '@mui/material';
import MoonLoader from 'react-spinners/MoonLoader';

export default function LoadingScreen({ message = 'Loading...' }) {
  //Loading screen with a little spinner animation- Endless looping animation
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        maxHeight: '100vh',
        overflow: 'hidden',
      }}
    >
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
        <Typography variant="h4" sx={{ mb: 2 }}>
          {message}
        </Typography>
      </Box>
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
        <MoonLoader 
          size={70}
          color="#ffffff"
          loading={true}
        />
      </Box>
    </Box>
  );
}
