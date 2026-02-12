import { useState, useEffect } from "react";

export default function OnlineLoadList({ onSelect, currentUserId }) {
    const [savedGames, setSavedGames] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        // fetch(`/api/games/user/${currentUserId}?state=IN_PROGRESS`)

        const fetchSavedGames = async () => {
            try {
                const mockData = [
                    { id: 101, name: "Esti Csata", creation_date: "2026-02-11", board_state: "XO_X_____" },
                    { id: 105, name: "Revans", creation_date: "2026-02-12", board_state: "_________" }
                ];
                setSavedGames(mockData);
            } catch (err) {
                console.error("Unsuccessfull load", err);
            } finally {
                setLoading(false);
            }
        };

        fetchSavedGames();
    }, [currentUserId]);
    return (
        <div className="loginPanel" style={{ opacity: 1 }}>
            <h2 className="helptext">RESUME ONLINE GAME</h2>
            <div className="loginForm">
                {loading ? (
                    <p style={{ color: 'aquamarine', textAlign: 'center' }}>Loading saves...</p>
                ) : savedGames.length > 0 ? (
                    savedGames.map(game => (
                        <div
                            key={game.id}
                            className="server-item"
                            style={{
                                border: '1px solid blueviolet',
                                padding: '10px',
                                marginBottom: '10px',
                                cursor: 'pointer'
                            }}
                            onClick={() => onSelect(game)}
                        >
                            <div style={{ fontWeight: 'bold' }}>{game.name}</div>
                            <div style={{ fontSize: '0.8rem', color: 'gray' }}>
                                Saved: {new Date(game.creation_date).toLocaleDateString()}
                            </div>
                        </div>
                    ))
                ) : (
                    <p style={{ color: 'gray', textAlign: 'center' }}>No active games found.</p>
                )}
            </div>
        </div>
    );
}