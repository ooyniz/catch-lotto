import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import "../styles/LoginModal.css";

const LoginModal = ({ isOpen, onClose, setIsLogin }) => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    if (!isOpen) return null; // 모달이 닫혀 있으면 렌더링 안 함
    const handleLogin = async () => {
        try {
            const res = await fetch("/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ username, password }),
                credentials: "include", // 쿠키 전송 시 필요
            });

            if (res.ok && res.headers.get("Authorization")) {
                const accessToken = res.headers.get("Authorization").replace("Bearer ", "");
                localStorage.setItem("accessToken", accessToken);
                setIsLogin(true);
                alert("로그인 성공!");
                navigate("/");
                onClose();
            } else {
                alert("로그인 실패!");
            }
        } catch (err) {
            console.error("로그인 에러", err);
            alert("서버 오류 발생!");
        }
    };
    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <button className="close-btn" onClick={onClose}>✖</button>
                <h2>로그인</h2>
                <input type="text"  placeholder="아이디" 
                    onChange={(e) => setUsername(e.target.value)} />
                <input type="password" placeholder="비밀번호" 
                    onChange={(e) => setPassword(e.target.value)} />
                <button className="login-btn" onClick={handleLogin}>로그인</button>
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