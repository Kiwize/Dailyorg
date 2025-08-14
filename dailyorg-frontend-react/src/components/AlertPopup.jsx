import { Box, Typography } from '@mui/material';
import { CheckIcon } from 'lucide-react';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';

const AlertPopup = ({ text, type }) => {
  if (!text || !type) {
    return null;
  }

  return (
    <Box
      sx={{
        padding: 2,
        backgroundColor: '#262626',
        borderRadius: 2,
        zIndex: 9999,
        width: 'fit-content',
        animation: 'slideIn 0.5s ease-in-out',
        '@keyframes slideIn': {
          '0%': { transform: 'translateX(100%)' },
          '100%': { transform: 'translateX(0)' },
        },
        overflow: 'hidden',
      }}
    >
      <Box sx={{ display: 'flex', alignItems: 'center' }}>
        {
          // Icon can be added here if needed
          <Box sx={{ mr: 1 }}>{type === 'error' ? <ErrorOutlineIcon sx={{ color: '#fff' }} /> : <CheckIcon sx={{ color: '#155724' }} />}</Box>
        }

        <Typography variant="h6" sx={{ color: type === 'error' ? '#fff' : '#00cc00' }}>
          {text}
        </Typography>
      </Box>
      <Box
        //Bar that indicates the remaining time before the alert disappears
        sx={{
          position: 'absolute',
          bottom: 0,
          left: 0,
          borderRadius: '0 0 2px 2px',
          width: '100%',
          height: 8,
          zIndex: 1,
          backgroundColor: type === 'error' ? 'red' : 'green',
          animation: 'progressBar 4s linear reverse',
          '@keyframes progressBar': {
            '100%': { width: '100%' },
            '0%': { width: '0%' },
          },
        }}
      ></Box>
    </Box>
  );
};

export default AlertPopup;
