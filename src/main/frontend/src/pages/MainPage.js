import React, { useEffect, useState } from 'react';
import '../styles/MainPage.css';

function MainPage() {
    const [count, setCount] = useState(10); // 기본 조회 범위
    const [stats, setStats] = useState([]);
    const [random, setRandom] = useState(null); // 추천 번호
    const [loading, setLoading] = useState(false);

    // 조회할 회차 수가 바뀔 때마다 API 호출
    useEffect(() => {
        const fetchStats = async () => {
            setLoading(true);
            try {
                const res = await fetch(`/api/lotto/stats?count=${count}`);
                const data = await res.json();
                setStats(data.data);
            } catch (err) {
                console.error('로또 통계 조회 실패:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, [count]);

    const fetchRandom = async () => {
        try {
            const res = await fetch('/api/lotto/random');
            const data = await res.json();
            setRandom(data.data);
        } catch (err) {
            console.error('랜덤 번호 가져오기 실패:', err);
        }
    };

    return (
        <div className="main-page">
            <main className="content">
                <h2>🎲 랜덤 추천 번호</h2>
                <button onClick={fetchRandom} style={{ marginBottom: '20px' }}>
                    추천 번호 보기
                </button>

                {random && (
                    <div style={{ display: 'flex', gap: '16px', justifyContent: 'center' }}>
                        {Object.values(random).map((num, idx) => (
                            <div key={idx} className="ball">{num}</div>
                        ))}
                    </div>
                )}
                
                <hr style={{ margin: '40px 0' }} />
                <h2>🎯 로또 번호 통계</h2>

                <label htmlFor="countSelect">조회할 회차 수: </label>
                <select
                    id="countSelect"
                    value={count}
                    onChange={(e) => setCount(e.target.value)}
                    style={{ marginBottom: '20px' }}
                >
                    <option value="10">최근 10회</option>
                    <option value="100">최근 100회</option>
                    <option value="1000">최근 1000회</option>
                </select>

                {loading ? (
                    <p>📡 로딩 중...</p>
                ) : (
                    <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap', justifyContent: 'center' }}>
                        {stats.map(({ number, count }) => (
                            <div key={number} className="ball-container">
                                <div className="ball">{number}</div>
                                <div>{count}회</div>
                            </div>
                        ))}
                    </div>
                )}
            </main>
        </div>
    );
}

export default MainPage;
