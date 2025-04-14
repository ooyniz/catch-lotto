import React from 'react';
import '../styles/Signup.css';

function MyPageForm({ formData, handleChange, handleUpdate, handleDelete, maxBirthDate }) {
    return (
        <form onSubmit={handleUpdate} className="signup-form">
            <h2>내 정보 수정</h2>

            <div className="form-group">
                <label>아이디</label>
                <input
                    type="text"
                    name="username"
                    value={formData.username}
                    disabled
                />
            </div>

            <div className="form-group">
                <label>이름</label>
                <input
                    type="text"
                    name="nickname"
                    value={formData.nickname}
                    onChange={handleChange}
                    placeholder="이름을 입력해 주세요"
                    required
                />
            </div>

            <div className="form-group">
                <label>생년월일</label>
                <input
                    type="date"
                    name="birth"
                    value={formData.birth || ''}
                    onChange={handleChange}
                    max={maxBirthDate}
                />
            </div>

            <div className="form-group gender-group">
                <label>성별</label>
                <div className="gender-options">
                    <label className={`gender-option ${formData.gender === 'm' ? 'selected' : ''}`}>
                        <input
                            type="radio"
                            name="gender"
                            value="m"
                            checked={formData.gender === 'm'}
                            onChange={handleChange}
                        />
                        남자
                    </label>
                    <label className={`gender-option ${formData.gender === 'f' ? 'selected' : ''}`}>
                        <input
                            type="radio"
                            name="gender"
                            value="f"
                            checked={formData.gender === 'f'}
                            onChange={handleChange}
                        />
                        여자
                    </label>
                </div>
            </div>

            <button type="submit" className="submit-button">
                정보 수정하기
            </button>

            <div style={{ marginTop: '2rem' }}>
                <button type="button" className="submit-button" style={{ backgroundColor: '#aaa' }} onClick={handleDelete}>
                    회원 탈퇴하기
                </button>
            </div>
        </form>
    );
}

export default MyPageForm;