import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import MainPage from './pages/MainPage';
import Signup from './pages/Signup';
import LoginModal from "./components/LoginModal";
import MyPage from './pages/MyPage';
import Header from "./components/Header"; // Header 컴포넌트 추가
import "./styles/LoginModal.css";
import useAutoLogin from "./hooks/useAutoLogin";

function App() {
  const [isLogin, setIsLogin] = useState(false);
  const [isModalOpen, setModalOpen] = useState(false); // 모달 상태 관리

  // useAutoLogin 훅을 사용하여 자동 로그인 처리
  useAutoLogin(setIsLogin);

  return (
    <Router>
      <div>
        {/* Header에 setModalOpen 전달 */}
        <Header isLogin={isLogin} setModalOpen={setModalOpen} setIsLogin={setIsLogin} />
        {/* 모달은 Routes 외부에 배치 */}
        <LoginModal isOpen={isModalOpen} onClose={() => setModalOpen(false)} setIsLogin={setIsLogin}/>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/signup"  element={<Signup setModalOpen={setModalOpen} />} />
          <Route path="/mypage"  element={<MyPage setIsLogin={setIsLogin} />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;