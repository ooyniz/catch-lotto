import React from 'react';
import '../styles/Header.css';
import { Link, useNavigate } from 'react-router-dom';

const Header = ({ isLogin, setModalOpen, setIsLogin }) => {
    const navigate = useNavigate();

    const handleLogout = async () => {
        const token = localStorage.getItem("accessToken");
      
        await fetch("/logout", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`, 
          },
          credentials: "include", 
        });
      
        localStorage.removeItem("accessToken");
        setIsLogin(false);
        alert("로그아웃 되었습니다.");
        navigate("/");
      };

    return (
        <header className="header">
            <Link to='/'>
                <img src="/catchlotto-banner.png" alt="Logo" className="logo" />
            </Link>
            <nav className="nav-links">
                {isLogin ? (
                        <><Link to="/mypage">My Page</Link><span onClick={handleLogout} className="login-link">Logout</span></>
                ) : (
                    <>
                        <span onClick={() => setModalOpen(true)} className="login-link">Login</span> /
                        <Link to="/signup">Signup</Link>
                    </>
                )}
            </nav>
        </header>
    );
}

export default Header;