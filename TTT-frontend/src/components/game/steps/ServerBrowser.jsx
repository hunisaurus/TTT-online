import { useState, useEffect } from "react";

export default function ServerBrowser({ onJoin }) {
    const [servers, setServers] = useState([]);

    useEffect(() => {
        // fetch('/api/games?state=WAITING').then(res => res.json()).then(setServers);


        setServers([{ id: 1, name: "Pro Match #1", creator: "Admin" }]);
    }, []);
    return (
        <div className="loginPanel" style={{ opacity: 1 }}>
            <h2 className="helptext">SERVERS</h2>
            {servers.map(server => (
                <div key={server.id} className="server-item" onClick={() => onJoin(server)}>
                    {server.name} - Join!
                </div>
            ))}
        </div>
    );
}