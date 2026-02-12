import { useState, useEffect } from "react";

export default function OnlineLoadList({ onSelect, currentUserId }) {
    const [savedGames, setSavedGames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        console.log("Fetch indítása, felhasználó:", currentUserId);
        const fetchSavedGames = async () => {
            try {
                setLoading(true);

                const response = await fetch(`http://localhost:8080/games?username=${currentUserId}`);

                if (!response.ok) throw new Error("Cant reach the games");
                const data = await response.json();

                setSavedGames(data);
            } catch (err) {
                console.error("Error in Loading:", err);
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        if (currentUserId) {
            fetchSavedGames();
        }
    }, [currentUserId]);

    return (
        <div className="loginPanel" style={{ opacity: 1 }}>
            <h2 className="helptext">RESUME ONLINE GAME</h2>
            <div className="loginForm">
                {loading && <p style={{ color: 'aquamarine', textAlign: 'center' }}>Loading...</p>}
                {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}

                {!loading && savedGames.length > 0 ? (
                    savedGames.map(game => (
                        <div
                            key={game.id}
                            className="server-item"
                            style={{ /* ... stílusok ... */ }}
                            onClick={() => onSelect(game)}
                        >
                            <div style={{ fontWeight: 'bold', color: 'white' }}>{game.name}</div>
                            <div style={{ fontSize: '0.8rem', color: '#ccc' }}>
                                State: {game.gameState} | Created: {game.timeCreated ? new Date(game.timeCreated).toLocaleDateString() : "Unknown"}
                            </div>
                        </div>
                    ))
                ) : (
                    !loading && <p style={{ color: 'gray', textAlign: 'center' }}>No active games found.</p>
                )}
            </div>
        </div>
    );
}