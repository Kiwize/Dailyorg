//This context provides a way to manage the state and behavior of the toolbar across the application.

import { Box, Typography, Button, IconButton } from '@mui/material';
import React, { createContext, useContext, useEffect, useState } from 'react';

import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import BrokenImageIcon from '@mui/icons-material/BrokenImage';

export const ToolbarContext = createContext(null);

export const ToolbarProvider = ({ children }) => {
  const [toolbarActions, setToolbarActions] = useState([]);
  const [toolbarEnabled, setToolbarEnabled] = useState(false);
  const [isToolbarVisible, setIsToolbarVisible] = useState(true);

  const updateToolbarActions = (actions) => {
    setToolbarActions(actions);
  };

  return (
    <ToolbarContext.Provider value={{ toolbarActions, updateToolbarActions, setToolbarEnabled }}>
      {toolbarEnabled && (
        <Box
          className="toolbar"
          sx={{
            backgroundColor: '#686868bb',
            position: 'fixed',
            borderRadius: '0 20px 20px 0',
            top: "25%",
            zIndex: 1000,
            width: '6%',
            left: isToolbarVisible ? 0 : '-6%',
            transition: 'left 0.3s ease',
            height: '50vh',
          }}
        >
          <Box className="toolbar-content">
            <Box className="toolbar-actions"
              sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: '10px' }}
            >
              {toolbarActions.map((action, index) => (
                <IconButton key={index} onClick={action.onClick}>
                  {/* Change icon depending on action type, support multiple icons */}
                  {action.icon ? (
                    <action.icon sx={{ color: 'white', fontSize: 40 }} />
                  ) : (
                    <BrokenImageIcon sx={{ color: 'white', fontSize: 40 }} />
                  )}
                </IconButton>
              ))}
            </Box>
          </Box>


          {/* Click to toggle toolbar, little handle always visible, when toggled off the toolbar retracts out of the viewport */}
          <Box
            sx={{
              position: 'absolute',
              top: '40%',
              right: '-32px',
              width: '32px',
              height: '20%',
              backgroundColor: '#585858bb',
              cursor: 'pointer',
              borderRadius: '0 10px 10px 0',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
            onClick={() => setIsToolbarVisible(!isToolbarVisible)}
          >
            <ArrowForwardIosIcon
              sx={{
                transform: isToolbarVisible ? 'rotate(180deg)' : 'rotate(0deg)',
                transition: 'transform 0.3s ease',
              }}
            />
          </Box>
            
        </Box>
      )}
      {children}
    </ToolbarContext.Provider>
  );
};

export const useToolbar = () => {
  return useContext(ToolbarContext);
};
