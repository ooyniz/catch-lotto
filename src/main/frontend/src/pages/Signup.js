import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Signup.css';
import Header from '../components/Header';

function Signup() {
    return (
        <div>
            <Header />
            <div className="signup-container">
                <h2>JOIN US</h2>
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
                        <label>비밀번호 확인</label>
                        <input type="password" placeholder="비밀번호 확인" />
                    </div>
                    <div>
                        <label>이름</label>
                        <input type="text" placeholder="이름" />
                    </div>
                    <button type="submit">가입하기</button>
                    <div>
                        <Link to="/login">Already a member? Login</Link>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Signup;