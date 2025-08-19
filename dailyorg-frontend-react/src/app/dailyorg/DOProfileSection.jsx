import { Box, List, ListItem, ListItemText } from '@mui/material';

export default function DOProfileSection({ organizerProfile }) {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'row', gap: 4 }}>
      <Box>
        <h2>Organizer profile</h2>
        <List>
          <ListItem>
            <ListItemText primary="Total EXP" secondary={`${organizerProfile.experiencePoints} xp`} />
          </ListItem>
          <ListItem>
            <ListItemText primary="Level" secondary={`${Math.floor(organizerProfile.experiencePoints / 500)}`} />
          </ListItem>
          <ListItem>
            <ListItemText primary="Remaining XP" secondary={`${500 - (organizerProfile.experiencePoints % 500)} xp`} />
          </ListItem>
        </List>
      </Box>
      <Box>
        <h2>Organizer settings</h2>
        <List>
          <ListItem>
            <ListItemText primary="First shown hour" secondary={`${organizerProfile.calendarFirstShownHour}`} />
          </ListItem>
          <ListItem>
            <ListItemText primary="Total shown hours" secondary={`${organizerProfile.calendarTotalShownHours}`} />
          </ListItem>
        </List>
      </Box>
    </Box>
  );
}
