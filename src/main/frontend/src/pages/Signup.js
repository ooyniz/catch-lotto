import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useState } from "react";
import axios from "axios";
import '../styles/Signup.css';
import SignupForm from '../components/SignupForm.js';

function Signup({ setModalOpen }) {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        birth: '',
        gender: ''
    });

    const navigate = useNavigate();
    const [passwordMatch, setPasswordMatch] = useState(true); // 비밀번호 일치 여부 상태
    const [isChecked, setIsChecked] = useState(false); // 중복 검사 완료 여부

    const handleUsernameCheck = async () => {
        if (!formData.username.trim()) {
            alert("아이디를 입력해 주세요.");
            return;
        }
    
        // 중복 검사 API 호출
        try {
            const response = await fetch(`/api/user/exists?username=${formData.username}`);
            const data = await response.json();
    
            alert(data.message);

            if (!data.exists) {
                setIsChecked(true); // 중복 검사 성공 시 체크 완료
            } else {
                setIsChecked(false); // 중복된 아이디면 다시 검사 필요
            }
        } catch (error) {
            console.error("오류 발생:", error);
            alert("아이디 중복 검사를 진행할 수 없습니다.");
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });

        if (name === "username") {
            setIsChecked(false);
        }

        // 비밀번호 일치 여부 실시간 체크
        if ((formData.confirmPassword.length !== 0) && (name === 'password' || name === 'confirmPassword')) {
            setPasswordMatch(
                name === 'password'
                    ? value === formData.confirmPassword
                    : value === formData.password
            );
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!isChecked) {
            alert("아이디 중복 검사를 먼저 진행해 주세요.");
            return;
        }
        if (!passwordMatch) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }
        try {
            await axios.post('/api/user/signup', formData);
            alert('회원가입이 완료되었습니다! 로그인 해주세요.');
            navigate('/');
            setModalOpen(true); // 로그인 모달 열기
        } catch (error) {
            console.error('There was an error!', error);
            alert('회원가입에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const today = new Date();
    const maxDate = new Date(today.getFullYear() - 5, today.getMonth(), today.getDate())
        .toISOString()
        .split('T')[0];

    return (
        <div className="signup-container">
            <div className="signup-content">
                <div className="signup-form-container">
                    <SignupForm
                        formData={formData}
                        handleChange={handleChange}
                        handleSubmit={handleSubmit}
                        passwordMatch={passwordMatch}
                        maxBirthDate={maxDate} // maxDate 전달
                        handleUsernameCheck={handleUsernameCheck} 
                        isChecked={isChecked}
                    />
                </div>
                <div className="signup-footer">
                    <p>이미 계정이 있으신가요? <button onClick={() => setModalOpen(true)}>로그인</button></p>
                </div>
            </div>            
        </div>
    );
}

export default Signup;