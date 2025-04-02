import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useState } from "react";
import axios from "axios";
import '../styles/Signup.css';
import SignupForm from '../components/SignupForm.js';

function Signup() {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        birth: '',
        gender: ''
    });

    const [passwordMatch, setPasswordMatch] = useState(true); // 비밀번호 일치 여부 상태
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });

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
        try {
            await axios.post('/api/user/signup', formData);
            alert('회원가입이 완료되었습니다! 로그인 해주세요.');
            navigate('/login'); // 로그인 페이지로 이동
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
                    />
                </div>
                <div className="signup-footer">
                    <p>이미 계정이 있으신가요? <Link to="/login">로그인</Link></p>
                </div>
            </div>
        </div>
    );
}

export default Signup;