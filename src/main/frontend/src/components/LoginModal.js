import React from "react";
import { Link } from "react-router-dom"; // Link 컴포넌트 추가
import "../styles/LoginModal.css";

const LoginModal = ({ isOpen, onClose }) => {
    if (!isOpen) return null; // 모달이 닫혀 있으면 렌더링 안 함

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <button className="close-btn" onClick={onClose}>✖</button>
                <h2>로그인</h2>
                <input type="text" placeholder="아이디" />
                <input type="password" placeholder="비밀번호" />
                <button className="login-btn">로그인</button>
                <p>
                    <Link to="/signup" onClick={onClose}>회원가입</Link> | 
                    {/* <Link to="/forgot-password">아이디/비밀번호 찾기</Link> */}
                </p>
                <div className="social-login">
                    <button className="naver">네이버로 시작하기</button>
                    <button className="kakao">카카오로 시작하기</button>
                    <button className="google">Google로 시작하기</button>
                </div>
            </div>
        </div>
    );
};

export default LoginModal; 