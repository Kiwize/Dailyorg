import { Box, Button, Typography } from '@mui/material';

export default function BackendUnreachable() {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        maxHeight: '100vh',
        minHeight: '100vh',
        overflow: 'hidden',
        backgroundImage: 'url(/images/beluga_large.webp)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <Box
        sx={{
          position: 'fixed',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          alignItems: 'center',
          justifyContent: 'center',
          display: 'flex',
          flexDirection: 'column',
          textAlign: 'center',
        }}
      >
        <Typography variant="h2" sx={{ mt: 20 }}>
          Sowwy, the server is unreachable...
        </Typography>
        <Typography variant="h5" sx={{ mt: 2 }}>
          Pwease try again later.
        </Typography>
        <Box sx={{ flexGrow: 1 }} />
        <Button variant="contained" size='large' sx={{ mt: 2, mb: 8 }} onClick={() => window.location.reload()}>
          Retry
        </Button>
      </Box>
    </Box>
  );
}
