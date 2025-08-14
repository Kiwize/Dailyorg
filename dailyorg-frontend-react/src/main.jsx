import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App.jsx';
import { AlertProvider } from './contexts/AlertProvider.jsx';
import { ToolbarProvider } from './contexts/ToolbarProvider.jsx';
import BackendChecker from './contexts/BackendChecker.jsx';

createRoot(document.getElementById('root')).render(
  <BackendChecker>
    <AlertProvider>
      <StrictMode>
        <ToolbarProvider>
          <App />
        </ToolbarProvider>
      </StrictMode>
    </AlertProvider>
  </BackendChecker>
);
