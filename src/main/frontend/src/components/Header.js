import React from 'react';
import '../styles/Header.css';
import { Link } from 'react-router-dom';

const Header = ({ setModalOpen }) => {
    return (
        <header className="header">
            <Link to='/'>
                <img src="/catchlotto-banner.png" alt="Logo" className="logo" />
            </Link>
            <nav className="nav-links">
                <span onClick={() => setModalOpen(true)} className="login-link">Login</span> /<Link to="/signup">Signup</Link>
            </nav>
        </header>
    );
}

export default Header;