import React from 'react';

function SignupForm({ formData, handleChange, handleSubmit, passwordMatch, maxBirthDate, handleUsernameCheck, isChecked }) {
    return (
        <form onSubmit={handleSubmit} className="signup-form">
            <h2>회원정보 입력</h2>
            <div className="form-group">
                <label>아이디</label>
                <div className="input-group">
                    <input
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        placeholder="아이디를 입력해 주세요"
                        required
                    />
                    <button
                        type="button"
                        onClick={handleUsernameCheck}
                        className="check-button"
                        disabled={isChecked} // 중복 검사
                    >
                        {isChecked ? "사용 가능" : "중복 검사"}
                    </button>
                </div>
            </div>
            <div className="form-group">
                <label>비밀번호</label>
                <input
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="비밀번호를 입력해 주세요"
                    required
                />
            </div>
            <div className="form-group">
                <label>비밀번호 재입력</label>
                <input
                    type="password"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    placeholder="비밀번호를 다시 입력해 주세요"
                    required
                    className={passwordMatch ? "" : "error-input"} // 테두리 색상 변경
                />
                {!passwordMatch && <p className="error-text">비밀번호가 일치하지 않습니다.</p>}
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
                <label>생년월일 (선택)</label>
                <input
                    type="date"
                    name="birth"
                    value={formData.birth}
                    onChange={handleChange}
                    max={maxBirthDate} // max 속성 추가
                />
            </div>
            <div className="form-group gender-group">
                <label>성별 (선택)</label>
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
                가입 완료하기
            </button>
        </form>
    );
}

export default SignupForm;