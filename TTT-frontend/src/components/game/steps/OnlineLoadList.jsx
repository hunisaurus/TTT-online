import {useState, useEffect} from "react";

export default function OnlineLoadList({onSelect, currentUserId}) {
    const [savedGames, setSavedGames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
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

        <div className="menu-step.container">
            <h2 className="helptext">RESUME ONLINE GAME</h2>
            <div className="menu-card list-card">
                <div className="load-list-wrapper">
                    {loading && <p className="status-msg loading">Loading...</p>}
                    {error && <p className="status-msg error">{error}</p>}

                    {!loading && savedGames.length > 0 ? (
                        savedGames.map(game => (
                            <div
                                key={game.id}
                                className="load-item"
                                onClick={() => onSelect(game)}
                            >
                                <div className="load-info">
                                    <div className="game-name">{game.name}</div>
                                    <div className="game-meta">
                                        State: {game.gameState} |
                                        Created: {game.timeCreated ? new Date(game.timeCreated).toLocaleDateString() : "Unknown"}
                                    </div>
                                </div>
                                <div className="load-action">RESUME</div>
                            </div>
                        ))
                    ) : (
                        !loading && <p className="status-msg empty">No active games found.</p>
                    )}
                </div>
            </div>
        </div>
    );
}