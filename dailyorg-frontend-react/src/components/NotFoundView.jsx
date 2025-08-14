import { Box, Button, Typography } from '@mui/material';

export default function NotFoundView() {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        height: '100vh',
        maxHeight: '100vh',
        overflow: 'hidden',
        textAlign: 'center',
      }}
    >
      <Typography variant="h3" sx={{ mb: 2 }}>404 - Not Found</Typography>
      <Typography variant="body1">The page you are looking for does not exist.</Typography>
      <Button
        variant="contained"
        sx={{ mt: 3 }}
        onClick={() => window.location.href = '/'}
      >
        Return home
      </Button>
    </Box>
  );
}
