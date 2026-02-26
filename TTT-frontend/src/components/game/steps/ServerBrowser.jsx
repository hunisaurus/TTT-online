import { useState, useEffect } from "react";
import { getAvailableGames } from "../../../service/gameService";

export default function ServerBrowser({ onJoin }) {
  const [servers, setServers] = useState([]);

  useEffect(() => {
    const loadServers = async () => {
      try {
        const games = await getAvailableGames();
        setServers(games);
      } catch (e) {
        console.error("Failed to load available games", e);
        setServers([]);
      }
    };
    loadServers();
  }, []);
  return (
    <div className="loginPanel" style={{ opacity: 1 }}>
      <h2 className="helptext">SERVERS</h2>
      {servers.map((server) => (
        <div
          key={server.gameId}
          className="server-item"
          onClick={() => onJoin(server)}
        >
          {server.gameName} created by {server.creator} - Join!
        </div>
      ))}
    </div>
  );
}
