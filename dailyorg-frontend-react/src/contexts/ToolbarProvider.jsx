//This context provides a way to manage the state and behavior of the toolbar across the application.

import { Box, Typography, Button } from '@mui/material';
import React, { createContext, useContext, useEffect, useState } from 'react';

export const ToolbarContext = createContext(null);

export const ToolbarProvider = ({ children }) => {
  const [toolbarTitle, setToolbarTitle] = useState('Default Title');
  const [toolbarActions, setToolbarActions] = useState([]);
  const [toolbarEnabled, setToolbarEnabled] = useState(false);

  const updateToolbarTitle = (title) => {
    setToolbarTitle(title);
  };

  const updateToolbarActions = (actions) => {
    setToolbarActions(actions);
  };

  return (
    <ToolbarContext.Provider value={{ toolbarTitle, toolbarActions, updateToolbarTitle, updateToolbarActions, setToolbarEnabled }}>
      <Box
        className="toolbar"
        sx={{
          position: 'fixed',
          top: 0,
          zIndex: 1000,
        }}
      >
        {toolbarEnabled && (
          <Box className="toolbar-content">
            <Typography variant="h6">{toolbarTitle}</Typography>
            <Box className="toolbar-actions">
              {toolbarActions.map((action, index) => (
                <Button key={index} onClick={action.onClick}>
                  {action.label}
                </Button>
              ))}
            </Box>
          </Box>
        )}
      </Box>

      {children}
    </ToolbarContext.Provider>
  );
};

export const useToolbar = () => {
  return useContext(ToolbarContext);
};
