import { useState, useEffect } from "react";

export default function CreateGame({ onContinue }) {
    const [isPrivate, setIsPrivate] = useState(false);
    const [roomName, setRoomName] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);

    const handleCreate = async () => {
        setLoading(true);
        try {
            // BACKEND
            // const response = await fetch('/api/games', {
            //     method: 'POST',
            //     body: JSON.stringify({ name: roomName, creator_id: userId, state: 'WAITING' })
            // });
            // const newGame = await response.json();

            const mockGame = { id: Math.floor(Math.random() * 1000), name: roomName };

            onContinue(mockGame);
        } catch (err) {
            console.error("Error in game creation", err);
        } finally {
            setLoading(false);
        }
    };




    return (
        <div className="loginPanel" style={{ opacity: 1 }}>
            <h2 className="helptext">CREATE GAME</h2>
            <input
                className="loginInput"
                placeholder="Room Name"
                onChange={(e) => setRoomName(e.target.value)}
            />
            <button className="loginButton" onClick={handleCreate} disabled={loading}>
                {loading ? "CREATING..." : "CONFIRM & GO TO TOKENS"}
            </button>
        </div>
    );
}