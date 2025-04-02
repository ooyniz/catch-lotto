import React from 'react';
import '../styles/Header.css';
import { Link } from 'react-router-dom';

function Header() {
    return (
        <header className="header">
            <Link to='/'>
                <img src="/catchlotto-banner.png" alt="Logo" className="logo" />
            </Link>
            <nav className="nav-links">
                <Link to="/login">Login</Link> / <Link to="/signup">Signup</Link>
            </nav>
        </header>
    );
}

export default Header;