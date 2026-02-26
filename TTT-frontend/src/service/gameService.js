export const joinQuickMatch = async (useName) => {};

export const createOnlineGame = async (
  userName,
  gameName,
  maxPlayerCount,
  character,
) => {
  const res = await fetch("http://localhost:8080/games/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      userName,
      gameName,
      maxPlayerCount,
      character,
    }),
  });
  if (!res.ok) throw new Error("Creation failed");
};

export const joinOnlineGame = async (character, gameId) => {
  const token = localStorage.getItem("jwt");
  const res = await fetch(`http://localhost:8080/games/${gameId}/join`, {
    method: "PATCH",
    headers: token
      ? { Authorization: `Bearer ${token}`, "Content-Type": "application/json" }
      : { "Content-Type": "application/json" },
    body: JSON.stringify({
      character: character,
    }),
  });
  if (!res.ok) throw new Error("Joining online game failed");
  return res.json();
};

export const startOnlineGame = async (gameId) => {
  const token = localStorage.getItem("jwt");
  const res = await fetch(`http://localhost:8080/games/${gameId}/start`, {
    method: "PATCH",
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });
  if (!res.ok) throw new Error("Starting online game failed");
  return res.json();
};

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
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });
  if (!res.ok) throw new Error("Getting available games failed");
  return res.json();
};

export const getGameStatus = async (gameId) => {
  const token = localStorage.getItem("jwt");
  const res = await fetch(`http://localhost:8080/games/${gameId}`, {
    method: "GET",
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });
  if (!res.ok) throw new Error(`Getting game #${gameId} failed`);
  return res.json();
};
