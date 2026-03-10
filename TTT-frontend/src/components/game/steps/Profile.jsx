import React, { useState, useEffect } from 'react';
import "./profile.css";

const Profile = ({ onBack }) => {
    const [userData, setUserData] = useState({
        username: localStorage.getItem('userName') || "Player",
        wins: 0,
        losses: 0,
        totalGames: 0
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                const token = localStorage.getItem('token');
                if (!token) {
                    console.error("There is no user token!");
                    setLoading(false);
                    return;
                }

                const response = await fetch('http://localhost:8080/user/me', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    setUserData({
                        username: data.username || userData.username,
                        wins: data.winCount || 0,
                        losses: (data.totalGames - data.winCount) || 0,
                        totalGames: data.totalGames || 0
                    });
                } else {
                    console.error("Server erro in user fetch:", response.status);
                }
            } catch (error) {
                console.error("network error:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchProfileData();
    }, []);

    const winRate = userData.totalGames > 0
        ? Math.round((userData.wins / userData.totalGames) * 100)
        : 0;

    return (
        <div className="profile-step-container fade-in-bottom">
            <div className="profile-dashboard">

                <div className="auth-card profile-info-card">
                    <div className="user-avatar-large">
                        {userData.username.charAt(0).toUpperCase()}
                    </div>
                    <h2 className="profile-username">{userData.username}</h2>
                    <p className="profile-role">Grandmaster</p>

                    <div className="auth-actions" style={{marginTop: 'auto', width: '100%'}}>
                        <button className="base-btn btn-ghost" onClick={onBack}>
                            BACK TO MENU
                        </button>
                    </div>
                </div>

                <div className="auth-card profile-stats-card">
                    <h3 className="helptext">STATISTICS</h3>

                    {loading ? (
                        <div className="stat-label" style={{textAlign: 'center', width: '100%', marginTop: '20px'}}>
                            UPDATING STATS...
                        </div>
                    ) : (
                        <>
                            <div className="stats-display-grid">
                                <div className="stat-box">
                                    <span className="stat-label">TOTAL</span>
                                    <span className="stat-value">{userData.totalGames}</span>
                                </div>
                                <div className="stat-box highlight">
                                    <span className="stat-label">WINS</span>
                                    <span className="stat-value">{userData.wins}</span>
                                </div>
                                <div className="stat-box">
                                    <span className="stat-label">LOSSES</span>
                                    <span className="stat-value">{userData.losses}</span>
                                </div>
                            </div>

                            <div className="winrate-section">
                                <div className="winrate-info">
                                    <span>WIN RATE</span>
                                    <span style={{color: 'var(--primary)', fontWeight: 'bold'}}>{winRate}%</span>
                                </div>
                                <div className="winrate-track">
                                    <div
                                        className="winrate-fill"
                                        style={{ width: `${winRate}%` }}
                                    ></div>
                                </div>
                            </div>
                        </>
                    )}
                </div>

            </div>
        </div>
    );
};

export default Profile;