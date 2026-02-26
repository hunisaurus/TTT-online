export const joinQuickMatch = async (useName) => {};

export const createOnlineGame = async (userName, gameName, maxPlayerCount) => {
  const res = await fetch("http://localhost:8080/games/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
            userName: userName,
            gameName: gameName,
            maxPlayerCount: maxPlayerCount
        }),
  });
  if (!res.ok) throw new Error("Creation failed");
};

export const joinOnlineGame = async (userName, character) => {
    const res = await fetch("http://localhost:8080/games/create", {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
            userName: userName,
            character: character
        }),
  });
  if (!res.ok) throw new Error("Creation failed");
  return res.json();
}

export const makeMove = async (gameId, { userName, br, bc, sr, sc }) => {
  const res = await fetch(`http://localhost:8080/games/${gameId}/move`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ userName, br, bc, sr, sc }),
  });
  if (!res.ok) throw new Error("Move failed");
  return res.json();
};

export const getAvailableGames = async () => {
  const token = localStorage.getItem("jwt");
  const res = await fetch("http://localhost:8080/games/available", {
    method: "GET",
    headers: token
      ? { Authorization: `Bearer ${token}` }
      : {},
  });
  if (!res.ok) throw new Error("Getting available games failed");
  return res.json();
};
