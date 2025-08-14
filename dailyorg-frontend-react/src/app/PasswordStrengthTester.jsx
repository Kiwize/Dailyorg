import { useEffect, useState } from 'react';

export default function PasswordStrengthTester({ password, updateCallback }) {
  const [strength, setStrength] = useState('');

  useEffect(() => {
    if (password.length < 6) {
      setStrength(0);
    } else if (password.length < 10) {
      setStrength(1);
    } else {
      setStrength(2);
    }

    if (updateCallback) {
      updateCallback(strength);
    }
  }, [password]);

  return (
    <div>
      <p style={{marginBottom: '10px'}}>Password Strength: {strength === 0 ? 'Faible' : strength === 1 ? 'Moyen' : 'Fort'}</p>
      <div style={{ backgroundColor: 'lightgray', width: '100%', height: '10px', borderRadius: '5px', marginBottom: '10px' }}>
        <div
          style={{
            //width animation
            transition: 'width 0.4s ease',
            width: strength === 0 ? '20%' : strength === 1 ? '50%' : '100%',
            height: '10px',
            backgroundColor: strength === 0 ? 'red' : strength === 1 ? 'yellow' : 'green',
            borderRadius: '5px',
          }}
        ></div>
      </div>
    </div>
  );
}
