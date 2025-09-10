import { grey } from '@mui/material/colors';
import { parseISO, set } from 'date-fns';
import React, { useState, useRef, use } from 'react';
import useAlert from '../../hooks/useAlert';

function TaskCard({
  task,
  handleTaskClick,
  updateTaskBeforeDrag,
  updateTaskAfterDrag,
  updateTaskWhileDrag,
  displayConfig,
}) {
  const [position, setPosition] = useState({ x: 0, y: 0 });
  const [dragging, setDragging] = useState(false);
  const alert = useAlert();
  const offset = useRef({ x: 0, y: 0 });
  const didDrag = useRef(false);

  const borderColor = task.taskCompleted ? '#4caf50' : '#f44336';

  const taskElem = useRef(null);
  const calculatePosition = useRef();
  // Calculate the position of the task based on start and end dates

  const getContrastingTextColor = (hex) => {
    // Retirer le "#"
    hex = hex.replace("#", "");

    // Convertir en R, G, B
    let r = parseInt(hex.substr(0, 2), 16);
    let g = parseInt(hex.substr(2, 2), 16);
    let b = parseInt(hex.substr(4, 2), 16);

    // Calculer la luminance perçue (formule simplifiée)
    let luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;

    // Si luminance > 0.5 -> texte noir, sinon texte blanc
    return luminance > 0.5 ? "#000000" : "#ffffff";
  }

  const onMouseDown = (e) => {
    setDragging(true);
    didDrag.current = false; // Reset drag flag
    // Calculate offset between cursor and element
    offset.current = {
      x: e.clientX - position.x,
      y: e.clientY - position.y,
    };
    // Prevent default to avoid text selection
    e.preventDefault();

    let taskElement = e.target.parentElement.parentElement;



    if (taskElement.id.startsWith('taskID-')) {
      taskElem.current = taskElement;
      updateTaskBeforeDrag(task, taskElement); // Prepare the task for dragging
    }
  };

  // Function to calculate new start and end dates based on mouse position
  // This function is called when the mouse moves over a date element in the calendar
  const calculateNewDates = (e, hoveredElement, task) => {
    if (displayConfig === undefined || displayConfig.displayedHours === undefined || displayConfig.firstDisplayedHour === undefined) {
      alert.setAlert('An error occurred while calculating new dates', 'error');
      return { newStartDate: task.start, newEndDate: task.end };
    }

    const dateStr = hoveredElement.id.replace('date-', '');

    const newStartDate = parseISO(dateStr);
    const newEndDate = new Date(newStartDate); // Maintain duration

    const rect = hoveredElement.getBoundingClientRect();
    const mouseY = e.clientY - rect.top;
    const totalHeight = rect.height;
    const taskDuration = task.end.getTime() - task.start.getTime();

    const newStartTime = Math.round((mouseY / totalHeight) * displayConfig.displayedHours * 60) + displayConfig.firstDisplayedHour * 60; // minutes in day

    // Round up to nearest 15 minutes
    const roundedStartTime = Math.ceil(newStartTime / 15) * 15;

    const newEndTime = roundedStartTime + Math.round((taskDuration / (1000 * 60)) * (totalHeight / rect.height));

    newStartDate.setHours(Math.floor(roundedStartTime / 60), roundedStartTime % 60, 0, 0);
    newEndDate.setHours(Math.floor(newEndTime / 60), newEndTime % 60, 0, 0);

    return { newStartDate, newEndDate };
  };

  // Helper to get top and height percentages for a task
  function getTaskPosition(start, end) {
    if (displayConfig === undefined || displayConfig.displayedHours === undefined || displayConfig.firstDisplayedHour === undefined) {
      alert.setAlert('An error occurred while displaying task...', 'error');
      return { top: '0%', height: '0%' };
    }

    const startHour = start.getHours() + start.getMinutes() / 60;
    const endHour = end.getHours() + end.getMinutes() / 60;

    //Exclude tasks that are outside the displayed hours
    if (startHour < displayConfig.firstDisplayedHour) {
      return { top: '110%', height: '0%' }; // Hide tasks outside the displayed hours
    }

    const top = ((startHour - displayConfig.firstDisplayedHour) / displayConfig.displayedHours) * 100;
    const height = ((endHour - startHour) / displayConfig.displayedHours) * 100;

    return { top: `${top}%`, height: `${height}%` };
  }

  const onMouseMove = (e) => {
    if (!dragging) return;

    setPosition({
      x: e.clientX - offset.current.x,
      y: e.clientY - offset.current.y,
    });

    didDrag.current = true; // Mark that we have moved

    const dateElement = e.target.closest('[id^="date-"]');
    if (dateElement) {
      const calculatedPosition = calculateNewDates(e, dateElement, task);
      calculatePosition.current = calculatedPosition;
      updateTaskWhileDrag(task, calculatedPosition.newStartDate, calculatedPosition.newEndDate, dateElement); // Update task position while dragging
    }
  };

  const onMouseUp = (e) => {
    setDragging(false);
    updateTaskAfterDrag(task, calculatePosition.current.newStartDate, calculatePosition.current.newEndDate); // Final update after dragging
  };

  // Attach global mouse move/up when dragging
  React.useEffect(() => {
    if (dragging) {
      window.addEventListener('mousemove', onMouseMove);
      window.addEventListener('mouseup', onMouseUp);
    } else {
      window.removeEventListener('mousemove', onMouseMove);
      window.removeEventListener('mouseup', onMouseUp);
    }
    return () => {
      window.removeEventListener('mousemove', onMouseMove);
      window.removeEventListener('mouseup', onMouseUp);
    };
  }, [dragging]);

  return (
    <div>
      {task.start && task.end && (
        <div
          onClick={(e) => {
            if (e.target.className.includes('task-grabber')) return; // Ignore clicks on the grabber
            handleTaskClick(task);
          }}
          style={{
            userSelect: 'none',
          }}
        >
          <div>
            <div
              id={`taskID-${task.taskId}`}
              style={{
                background: task.category ? task.category.taskCategoryColor : grey[500],
                color: '#fff',
                borderRadius: 4,
                padding: '0px 0px',
                fontSize: 12,
                zIndex: 5,
                boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
                paddingTop: '4px',
                left: 2,
                right: 2,
                ...getTaskPosition(task.start, task.end),
                display: 'flex',
                alignItems: 'center',
                position: 'absolute',
              }}
            >
              <div
                style={{
                  width: '100%',
                  height: '100%',
                  //Except for the top border
                  borderLeft: `3px solid ${borderColor}`,
                  borderRight: `3px solid ${borderColor}`,
                  borderBottom: `3px solid ${borderColor}`,
                }}
              >
                <div
                  className="task-grabber"
                  style={{
                    cursor: 'grab',
                    position: 'absolute',
                    left: 0,
                    right: 0,
                    top: '0%',
                    background: 'gray',
                    borderRadius: 4,
                    padding: '3% 0px',
                  }}
                  onMouseDown={onMouseDown}
                ></div>
                {/* Text automatically takes the opposite color of the task's category color */}
                <p className="task-name" style={{ margin: 0, color: getContrastingTextColor(task.category ? task.category.taskCategoryColor : grey[500]), textAlign: 'center', pointerEvents: 'none', padding: '0 4px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                  {task.taskName}
                </p>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default TaskCard;
