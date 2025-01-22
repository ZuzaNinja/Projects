// App.js

import React, { useState } from 'react';
import Navbar from './components/Navbar'; // Adjust the path based on your project structure
import './App.css';
import arrow from './Icons/ilu.png';

function App() {
  const defaultText = 'Enter your text here';
  const [textBoxValue, setTextBoxValue] = useState(defaultText);
  const [showContent, setShowContent] = useState(true);
  const [showRectangle, setShowRectangle] = useState(false);
  const [responseText, setResponseText] = useState('');
  const [loading, setLoading] = useState(false);

  const handleTextBoxChange = () => {
    if (textBoxValue === defaultText) {
      setTextBoxValue('');
    }
  };

  const handleTextBoxBlur = () => {
    if (textBoxValue === '') {
      setTextBoxValue(defaultText);
    }
  };

  const handleButtonClick = async () => {
    setShowContent(!showContent);
    setLoading(true);

    const currentText = document.getElementById('textBox').value;

    try {
      const response = await fetch('http://localhost:8080/recommendation', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: currentText,
      });

      if (response.ok) {
        const data = await response.json();
        console.log('Response from back-end:', data);

        // Log the accessibility score
        console.log('Accessibility Score:', data.accessibilityScore);

        setResponseText(data);

        setTimeout(() => {
          setLoading(false);
          setShowRectangle(true);
        }, 1000);
      } else {
        console.error('Error:', response.statusText);
        setLoading(false);
      }
    } catch (error) {
      console.error('Error:', error);
      setLoading(false);
    }
  };

  console.log('Rendered with responseText:', responseText);

  return (
    <>
      <Navbar />
      <div className={`fade-container ${showContent ? 'fade-in' : 'fade-out'}`}>
        <div className="hh1">Inclusivity made easy: transform </div>
        <div className="hh1">your website, empower everyone</div>
        <div className="card">
          <label htmlFor="textBox"></label>
          <div className="input-container">
            <input
              type="text"
              id="textBox"
              value={textBoxValue}
              onChange={(e) => setTextBoxValue(e.target.value)}
              onClick={handleTextBoxChange}
              onBlur={handleTextBoxBlur}
            />
            <div className="image-container">
              <img src={arrow} alt="Arrow Icon" />
            </div>
            <button className="error-button" onClick={handleButtonClick}></button>
          </div>
        </div>
      </div>
      {showRectangle && (
        <div className={`rectangle fade-in-fluid`}>
          <div className="response-details">
            <p>
              <span className="number">{responseText.rating}</span>
              <span className="spacer"></span>
              <span className="spacer"></span>
              <span className="spacer"></span>
              <span className="spacer"></span>
              <span className="spacer"></span>
              <span className="spacer"></span>
              <span className="spacer"></span>
              <span className="number">{responseText.accessibilityScore}</span>
            </p>
            <p>
              <strong>Rating</strong>
              <span className="spacer"></span>
              <span className="spacer"></span>
              <strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Accessibility Score</strong>
            </p>
            {/* Add similar structures for other pairs */}
          </div>
          <p><strong>Basic Recommendations:</strong> {responseText.basicRecommendations}</p>
          <p><strong>Layout Recommendations:</strong> {responseText.layoutRecommendations}</p>
          <p><strong>Visibility Recommendations:</strong> {responseText.visibilityRecommendations}</p>
          <p><strong>Language Recommendations:</strong> {responseText.languageRecommendations}</p>
          <p><strong>Disabilities Affected:</strong> {responseText.disabilitiesAffected}</p>
        </div>
      )}
      {loading && (
        <div className="loading-container">
          <div className="loading-spinner"></div>
        </div>
      )}
    </>
  );
}

export default App;