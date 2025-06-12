import React, { useState } from 'react';

const SimpleCalendar = () => {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [selectedDate, setSelectedDate] = useState(null);

  const startOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
  const endOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
  const startDay = startOfMonth.getDay(); // Day of the week (0-6)
  const daysInMonth = endOfMonth.getDate();

  const prevMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1));
  };

  const nextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
  };

  const selectDate = (day) => {
    setSelectedDate(new Date(currentDate.getFullYear(), currentDate.getMonth(), day));
  };

  // Generate array for calendar days
  const calendarDays = [];
  for (let i = 0; i < startDay; i++) {
    calendarDays.push(null); // Empty cells for days before the start of the month
  }
  for (let i = 1; i <= daysInMonth; i++) {
    calendarDays.push(i);
  }

  return (
    <div style={styles.calendar}>
      <div style={styles.header}>
        <button onClick={prevMonth}>&lt;</button>
        <h2>
          {currentDate.toLocaleString('default', { month: 'long' })} {currentDate.getFullYear()}
        </h2>
        <button onClick={nextMonth}>&gt;</button>
      </div>
      <div style={styles.weekdays}>
        {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map((day) => (
          <div key={day} style={styles.weekday}>
            {day}
          </div>
        ))}
      </div>
      <div style={styles.days}>
        {calendarDays.map((day, index) => (
          <div
            key={index}
            style={{
              ...styles.day,
              backgroundColor:
                selectedDate &&
                day &&
                selectedDate.getDate() === day &&
                selectedDate.getMonth() === currentDate.getMonth() &&
                selectedDate.getFullYear() === currentDate.getFullYear()
                  ? '#add8e6'
                  : '#fff',
            }}
            onClick={() => day && selectDate(day)}
          >
            {day}
          </div>
        ))}
      </div>
    </div>
  );
};

// Basic inline styles
const styles = {
  calendar: {
    width: '300px',
    margin: '0 auto',
    fontFamily: 'Arial, sans-serif',
  },
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  weekdays: {
    display: 'flex',
    justifyContent: 'space-between',
    fontWeight: 'bold',
  },
  weekday: {
    width: '14.28%',
    textAlign: 'center',
  },
  days: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  day: {
    width: '14.28%',
    height: '40px',
    lineHeight: '40px',
    textAlign: 'center',
    cursor: 'pointer',
    border: '1px solid #ddd',
  },
};

export default SimpleCalendar;
