import {useState, useEffect} from "react";

export default function ServerBrowser({onJoin}) {
    const [servers, setServers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // fetch('/api/games?state=WAITING').then(res => res.json()).then(setServers);
        setTimeout(() => {
            setServers([
                {id: 1, name: "Pro Match #1", currentPlayers: 1, maxPlayers: 2, status: "WAITING"},
                {id: 2, name: "Casual Play", currentPlayers: 2, maxPlayers: 3, status: "WAITING"},
                {id: 3, name: "Testing Room", currentPlayers: 1, maxPlayers: 2, status: "WAITING"}
            ]);
            setLoading(false);
        }, 800);
    }, []);
    return (
        <div className="menu-step-container">
            <h2 className="helptext">SERVER BROWSER</h2>

            <div className="menu-card list-card">
                <div className="server-list-wrapper">
                    {loading && <p className="status-msg loading">Scanning for matches...</p>}

                    {!loading && servers.length > 0 ? (
                        servers.map(server => (
                            <div
                                key={server.id}
                                className="server-browser-item"
                                onClick={() => onJoin(server)}
                            >
                                <div className="server-main-info">
                                    <div className="server-name">{server.name}</div>
                                    <div className="server-details">
                                        <span className="player-count">
                                            {server.currentPlayers} / {server.maxPlayers} Players
                                        </span>
                                        <span className="divider">|</span>
                                        <span className="server-status">{server.status}</span>
                                    </div>
                                </div>

                                <button className="base-btn btn-primary join-btn">
                                    JOIN
                                </button>
                            </div>
                        ))
                    ) : (
                        !loading && <p className="status-msg empty">No active servers found.</p>
                    )}
                </div>
            </div>
        </div>
    );
}