//Toolbar context hook for managing toolbar state in the application.
import { useContext } from 'react';
import { ToolbarContext } from '../contexts/ToolbarProvider';

const useToolbar = () => {
  const context = useContext(ToolbarContext);
    if (!context) {
        throw new Error("useToolbar must be used within a ToolbarProvider");
    }
    return context;
};

export default useToolbar;