import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import MyPageForm from '../components/MyPageForm';

function MyPage({ setIsLogin }) {
    const [formData, setFormData] = useState({
        username: '',
        nickname: '',
        birth: '',
        gender: ''
    });

    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserInfo = async () => {
          try {
            const token = localStorage.getItem("accessToken");
            const res = await fetch("/api/user/me", {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            });
      
            const data = await res.json();
            if (res.ok && data.status === 200) {
              setFormData(data.data); // ✅ 성공 시 formData에 유저 정보 저장
            } else {
              alert("로그인이 만료되었습니다.");
              navigate("/");
            }
          } catch (err) {
            alert("유저 정보를 불러올 수 없습니다.");
            navigate("/");
          }
        };
      
        fetchUserInfo();
      }, [navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleUpdate = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem('accessToken');
            const response = await fetch('/api/user/update', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(formData)
            });

            const data = await response.json();
            if (data.status === 200) {
                alert("정보가 수정되었습니다.");
            } else {
                alert("수정 실패: " + data.message);
            }
        } catch (error) {
            alert("에러 발생: " + error);
        }
    };

    const handleDelete = async () => {
        if (!window.confirm("정말 탈퇴하시겠습니까?")) return;

        try {
            const token = localStorage.getItem('accessToken');
            const response = await fetch('/api/user/delete', {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            const data = await response.json();
            if (data.status === 200) {
                alert("회원 탈퇴가 완료되었습니다.");
                localStorage.removeItem("accessToken");
                setIsLogin(false);
                navigate('/');
            } else {
                alert("탈퇴 실패: " + data.message);
            }
        } catch (error) {
            alert("에러 발생: " + error);
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
                    <MyPageForm
                        formData={formData}
                        handleChange={handleChange}
                        handleUpdate={handleUpdate}
                        handleDelete={handleDelete}
                        maxBirthDate={maxDate}
                    />
                </div>
            </div>
        </div>
    );
}

export default MyPage;
