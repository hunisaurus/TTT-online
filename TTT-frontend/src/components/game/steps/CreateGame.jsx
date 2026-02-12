import { useState } from "react";

export default function CreateGame({ onContinue }) {
    const [formData, setFormData] = useState({
        gameName: "",
        maxPlayerCount: 2
    });
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        const storedUserName = localStorage.getItem('userName');

        const requestBody = {
            userName: storedUserName,
            gameName: formData.gameName,
            maxPlayerCount: parseInt(formData.maxPlayerCount)
        };

        try {
            const response = await fetch("http://localhost:8080/games/create", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody),
            });

            if (response.ok) {
                onContinue();
            } else {
                alert("Error in creation");
            }
        } catch (error) {
            console.error("Cant reach the backend! :", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="loginPanel" style={{ opacity: 1 }}>

            <form onSubmit={handleSubmit} className="loginForm">
                <input
                    className="loginInput"
                    placeholder="Game Name"
                    required
                    value={formData.gameName}
                    onChange={(e) => setFormData({...formData, gameName: e.target.value})}
                />

                <div className="form-group" style={{marginTop: "15px"}}>
                    <label style={{color: "white", marginRight: "10px"}}>Players:</label>
                    <select
                        value={formData.maxPlayerCount}
                        onChange={(e) => setFormData({...formData, maxPlayerCount: e.target.value})}
                    >
                        <option value="2">2 Players</option>
                        <option value="3">3 Players</option>
                    </select>
                </div>

                <button type="submit" className="loginButton" disabled={loading}>
                    {loading ? "CREATING..." : "Loading..."}
                </button>
            </form>
        </div>
    );
}