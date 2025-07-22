import { parseISO, set } from 'date-fns';
import React, { useState, useRef } from 'react';

function TaskCard({ task, getTaskPosition, handleTaskClick, updateTaskBeforeDrag, updateTaskAfterDrag, updateTaskWhileDrag }) {
  const [position, setPosition] = useState({ x: 0, y: 0 });
  const [dragging, setDragging] = useState(false);
  const offset = useRef({ x: 0, y: 0 });
  const didDrag = useRef(false);

  // Calculate the position of the task based on start and end dates

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
    updateTaskBeforeDrag(task); // Prepare the task for dragging
  };

  // Function to calculate new start and end dates based on mouse position
  // This function is called when the mouse moves over a date element in the calendar
  const calculateNewDates = (e, hoveredElement, task) => {
    const dateStr = hoveredElement.id.replace('date-', '');

    const newStartDate = parseISO(dateStr);
    const newEndDate = new Date(newStartDate); // Maintain duration

    const rect = hoveredElement.getBoundingClientRect();
    const mouseY = e.clientY - rect.top;
    const totalHeight = rect.height;
    const taskDuration = task.end.getTime() - task.start.getTime();

    const newStartTime = Math.round((mouseY / totalHeight) * 24 * 60); // minutes in day

    // Round up to nearest 15 minutes
    const roundedStartTime = Math.ceil(newStartTime / 15) * 15;

    const newEndTime = roundedStartTime + Math.round((taskDuration / (1000 * 60)) * (totalHeight / rect.height));

    newStartDate.setHours(Math.floor(roundedStartTime / 60), roundedStartTime % 60, 0, 0);
    newEndDate.setHours(Math.floor(newEndTime / 60), newEndTime % 60, 0, 0);

    return { newStartDate, newEndDate };
  };

  const onMouseMove = (e) => {
    if (!dragging) return;

    setPosition({
      x: e.clientX - offset.current.x,
      y: e.clientY - offset.current.y,
    });

    didDrag.current = true; // Mark that we have moved

    updateTaskWhileDrag(task, e.clientX, e.clientY, e.target); // Update task position while dragging
  };

  const onMouseUp = (e) => {
    setDragging(false);

    //Check if the mouse hovers one of the days in the calendar
    //Each day is a Paper component with an id of "date-{date} a.k.a. "date-2025-07-01"
    //Depending on the Y position of the mouse on the date element, we can determine the start and end time of the task
    var hoveredElement = document.elementFromPoint(e.clientX, e.clientY);

    if (hoveredElement && hoveredElement.id.startsWith('taskID-')) {
      // If the mouse is hovering over another task, we simply get the hovered task's parent to update the task position
      const parent = hoveredElement.parentElement.parentElement.parentElement;
      hoveredElement = parent;
    }

    if (hoveredElement && hoveredElement.id.startsWith('date-')) {
      const result = calculateNewDates(e, hoveredElement, task);

      // Call the update function to save the new task position
      updateTaskAfterDrag(task, result.newStartDate, result.newEndDate); // Final update after dragging
    }
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
          onMouseDown={onMouseDown}
          onClick={() => {
            if (!didDrag.current) handleTaskClick(task);
          }}
          style={{
            cursor: 'grab',
            userSelect: 'none',
          }}
        >
          <div
            id={`taskID-${task.taskId}`}
            style={{
              background: '#1976d2',
              color: '#fff',
              borderRadius: 4,
              padding: '2px 6px',
              fontSize: 12,
              zIndex: 5,
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              left: 2,
              right: 2,
              ...getTaskPosition(task.start, task.end),
              display: 'flex',
              alignItems: 'center',
              position: 'absolute',
            }}
          >
            <p style={{ margin: 0 }}>{task.taskName}</p>
            <div
              style={{
                position: 'absolute',
                left: 0,
                right: 0,
                top: '96%',
                background: task.taskCompleted ? '#4caf50' : '#f44336',
                color: '#fff',
                borderRadius: 4,
                padding: '4% 6px',
                fontSize: 12,
                zIndex: 6,
              }}
            ></div>
          </div>
        </div>
      )}
    </div>
  );
}

export default TaskCard;
