import React from 'react';
import './Navbar.css';
import heartIcon from '../Icons/LOGOpng.png'; // Update the path accordingly

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="logo-container">
        <div className="logo">
          <img src={heartIcon} alt="Heart Icon" />
        </div>
        <div className="site-name">InclusiWeb</div>
      </div>
      <ul className="nav-list">
        <li className="nav-item underline"><a href="#">Home</a></li>
        <li className="nav-item underline"><a href="#">Products</a></li>
        <li className="nav-item underline"><a href="#">About</a></li>
        <li className="nav-item underline"><a href="#">Contact us</a></li>
        <li className="nav-item"><a href="#" className="login-button">Login</a></li>
        <li className="nav-item"><a href="#" className="register-button">Register</a></li>
      </ul>
    </nav>
  );
};

export default Navbar;
