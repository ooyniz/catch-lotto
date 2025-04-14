import { useEffect } from "react";

function useAutoLogin(setIsLogin) {
  useEffect(() => {
    const validateToken = async () => {
      const token = localStorage.getItem("accessToken");
      if (!token) {
        setIsLogin(false);
        return;
      }

      try {
        const res = await fetch("/validate", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
          credentials: "include",
        });

        const data = await res.json();

        if (res.ok && data.data === true) {
          setIsLogin(true); // 토큰 유효, 로그인 유지
        } else {
          throw new Error("Access Token invalid");
        }
      } catch (err) {
        console.warn("Access Token invalid, trying reissue...");
        await tryReissue();
      }
    };

    const tryReissue = async () => {
      try {
        const res = await fetch("/reissue", {
          method: "POST",
          credentials: "include",
        });

        const data = await res.json();

        if (res.ok && data.data.accessToken) {
          localStorage.setItem("accessToken", data.data.accessToken);
          setIsLogin(true); // 재발급 성공 -> 로그인 유지
        } else {
          throw new Error("Reissue failed");
        }
      } catch (err) {
        console.error("재발급 실패. 로그아웃 처리");
        localStorage.removeItem("accessToken");
        setIsLogin(false);
      }
    };

    validateToken();
  }, [setIsLogin]);
}

export default useAutoLogin;
