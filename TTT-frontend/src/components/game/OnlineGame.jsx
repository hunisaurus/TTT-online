import { useState, useMemo, useEffect } from "react";
import GiantBoard from "./GiantBoard";
import { useAudio } from "../../hooks/useAudio";
import { useWebSocket } from "../../state/WebSocketContext";
import { getWinner, isFull3 } from "../../state/gameLogic";
import { getGameStatus, startOnlineGame } from "../../service/gameService";

export default function OnlineGame({ config, onExit }) {
  const [state, setState] = useState(null);
  const { play } = useAudio();
  const [boardEntering, setBoardEntering] = useState(false);
  const [playersEntering, setPlayersEntering] = useState(false);
  const { subscribe, send } = useWebSocket();
  const [loading, setLoading] = useState(true);
  const [myTurn, setMyTurn] = useState(true);
  const userName = localStorage.getItem("userName");

  useEffect(() => {
    console.log("currentPlayer from server:", state?.currentPlayer);
    console.log("local userName:", localStorage.getItem("userName"));
    console.log("myTurn:", !!myTurn);
  }, [state?.currentPlayer]);
  
  useEffect(() => {
    setMyTurn(state?.currentPlayer && state.currentPlayer.username == userName);
  }, [state]);

  useEffect(() => {
    if (!config?.gameId) return;
    const loadState = async () => {
      setLoading(true);
      try {
        const gameState = await getGameStatus(config.gameId);

        const activeBigs = new Set(gameState.activeBoards ?? []);
        setState({
          ...gameState,
          activeBigs,
        });
      } catch (error) {
        console.error("Cant reach the backend! :", error);
      } finally {
        setLoading(false);
      }
    };

    loadState();

    const sub = subscribe(`/topic/games/${config.gameId}`, (msg) => {
      let body;
      try {
        body = JSON.parse(msg.body);
      } catch (err) {
        console.error("Invalid WS message JSON:", err, msg.body);
        return;
      }

      // Defensive defaults
      const smallBoards = body.smallBoards ?? null;
      const bigBoard = body.bigBoard ?? null;
      const activeBoardsArr = body.activeBoards ?? [];
      const activeBigs = new Set(activeBoardsArr);
      const currentPlayer = body.currentPlayer ?? null;
      const winner = body.winner ?? null;
      const rotation = body.rotation ?? [];
      const started = !!body.started; // coerce to boolean

      // Merge with previous state to avoid losing other fields
      setState((prev) => ({
        ...prev,
        smallBoards: smallBoards,
        bigBoard: bigBoard,
        activeBigs: activeBigs,
        currentPlayer: currentPlayer,
        winner: winner,
        rotation: rotation,
        started: started,
      }));
    });

    return () => {
      try {
        sub && sub.unsubscribe && sub.unsubscribe();
      } catch (err) {
        console.warn("Failed to unsubscribe:", err);
      }
    };
  }, [config?.gameId, subscribe]);

  useEffect(() => {
    console.log("Updated game state:", state);
  }, [state]);

  useEffect(() => {
    if (state?.started) {
      setBoardEntering(true);
      setPlayersEntering(true);
    }
  }, [state?.started]);

  useEffect(() => {
    if (config && state && boardEntering) {
      const t = setTimeout(() => setBoardEntering(false), 30);
      return () => clearTimeout(t);
    }
  }, [config, state, boardEntering]);

  useEffect(() => {
    if (config && state && playersEntering) {
      const t = setTimeout(() => setPlayersEntering(false), 30);
      return () => clearTimeout(t);
    }
  }, [config, state, playersEntering]);

  const resolvedWinner = useMemo(() => {
    if (!state || !state.winner) return false;
    if (state.winner) return state.winner.character;
  }, [state]);

  const resolvedDraw = useMemo(() => {
    if (!state || !state.bigBoard) return false;
    if (getWinner(state.bigBoard)) return false;
    return isFull3(state.bigBoard);
  }, [state]);

  const handlePlay = (br, bc, sr, sc) => {
    if (!state) return;
    if (!myTurn) return; // not your turn
    if (!state.activeBigs?.has(`${br},${bc}`)) return;

    const sb = state.smallBoards.map((row) =>
      row.map((b) => b.map((r) => [...r])),
    );

    if (sb[br][bc][sr][sc]) {
      play("noclick");
      return;
    }
    play("click");

    send("/app/games/move", {
      gameId: config.gameId,
      userName,
      br,
      bc,
      sr,
      sc,
    });
  };

  const onHover = () => play("hover");

  if (!config || !state) return null;

  return (
    <>
      {loading && (
        <main>
          <div>Loading...</div>
        </main>
      )}
      {!loading && state.started ? (
        <main>
          <div
            id="playerOneElement"
            className={`playerElement leftPlayer ${playersEntering ? "outLeft" : ""} ${state?.currentPlayer?.character === state.rotation[0] ? "activePlayer" : ""}`}
          >
            {state.rotation[0]}
          </div>
          <div
            id="playerTwoElement"
            className={`playerElement rightPlayer ${playersEntering ? "outRight" : ""} ${state?.currentPlayer?.character === state.rotation[1] ? "activePlayer" : ""}`}
          >
            {state.rotation[1]}
          </div>
          {state.rotation.length === 3 && (
            <div
              id="playerThreeElement"
              className={`playerElement rightPlayer ${playersEntering ? "outAbove" : ""} ${state?.currentPlayer?.character === state.rotation[2] ? "activePlayer" : ""}`}
              style={{ top: "12%" }}
            >
              {state.rotation[2]}
            </div>
          )}

          {!resolvedWinner && !resolvedDraw && (
            <GiantBoard
              smallBoards={state.smallBoards}
              bigBoard={state.bigBoard}
              activeBigs={state.activeBigs}
              canPlay={myTurn}
              onPlay={handlePlay}
              onHover={onHover}
              entering={boardEntering}
            />
          )}

          {(resolvedWinner || resolvedDraw) && (
            <div
              className={resolvedWinner ? "wonBigBoard" : "drawBigBoard"}
              onClick={(e) => {
                e.currentTarget.classList.add("fade-out");
                setTimeout(() => {
                  if (onExit) onExit();
                }, 500);
              }}
            >
              {resolvedWinner ? resolvedWinner : state.rotation.join("/")}
            </div>
          )}
        </main>
      ) : (
        <main>
          {!loading && (
            <h2 className={`helptext`}>WAITING FOR GAME TO START</h2>
          )}
          {localStorage.getItem("userName") == config.creator &&
            state?.rotation?.length > 1 && (
              <button
                className="base-btn btn-primary"
                onMouseOver={() => play("hover")}
                onClick={async () => {
                  play("gamestart");
                  try {
                    await startOnlineGame(config.gameId);
                  } catch (error) {
                    console.error("Can't start online game: " + error);
                  }
                }}
              >
                START GAME
              </button>
            )}
        </main>
      )}
      {!loading && (
        <button className="back-button-modern" onClick={onExit}>
          <span className="arrow-icon">←</span>
        </button>
      )}
    </>
  );
}
