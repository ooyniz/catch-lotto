import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Login.css';
import Header from '../components/Header';

function Login() {
    return (
        <div>
            <Header />
            <div className="login-container">
                <h2>LOGIN</h2>
                <form>
                    <div>
                        <label>아이디</label>
                        <input type="text" placeholder="아이디" />
                    </div>
                    <div>
                        <label>비밀번호</label>
                        <input type="password" placeholder="비밀번호" />
                    </div>
                    <div>
                        <input type="checkbox" id="remember" />
                        <label htmlFor="remember">아이디 저장</label>
                    </div>
                    <button type="submit">LOGIN</button>
                    <div>
                        <Link to="/signup">JOIN US</Link>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Login;