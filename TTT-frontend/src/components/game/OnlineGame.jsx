import { useState, useMemo, useEffect } from "react";
import GiantBoard from "./GiantBoard";
import { useAudio } from "../../hooks/useAudio";
import { useGameSocket } from "../../state/WebSocketContext";
import {
  getWinner,
  isFull3,
  nextActiveFromCell,
  anyPlayableBigs,
} from "../../state/gameLogic";
import "../../styles.css";
import { makeMove } from "../../service/gameService";

export default function OnlineGame({ config, onExit }) {
  const [state, setState] = useState(null);
  const { play } = useAudio();
  const [boardEntering, setBoardEntering] = useState(false);
  const [playersEntering, setPlayersEntering] = useState(false);
  const { subscribe } = useWebSocket();

  useEffect(() => {
    if (!config?.gameId) return;

    const sub = subscribe(`/topic/games/${config.gameId}`, (msg) => {
      const body = JSON.parse(msg.body);

      setState({
        smallBoards: body.smallBoards,
        bigBoard: body.bigBoard,
        activeBigs:
          body.currentPlayer.userName === localStorage.getItem("userName")
            ? new Set(body.activeBoards)
            : new Set([]),
        currentPlayer: body.currentPlayer,
        winner: body.winner || null,
      });
    });

    return () => sub?.unsubscribe?.();
  }, [config?.gameId, subscribe]);

  useEffect(() => {
    setBoardEntering(true);
    setPlayersEntering(true);
  }, [config]);

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
    if (!state) return false;
    if (getWinner(state.bigBoard)) return false;
    return isFull3(state.bigBoard);
  }, [state]);

  // TODO: update
  const handlePlay = (br, bc, sr, sc) => {
    if (!state) return;
    if (!state.activeBigs.has(`${br},${bc}`)) return;

    const sb = state.smallBoards.map((row) =>
      row.map((b) => b.map((r) => [...r])),
    );

    if (sb[br][bc][sr][sc]) {
      play("noclick");
      return;
    }
    play("click");
    sb[br][bc][sr][sc] = currentPlayer.character;

    const smallWinner = getWinner(sb[br][bc]);

    if (smallWinner) {
      bb[br][bc] = smallWinner;
    } else if (isFull3(sb[br][bc])) {
      bb[br][bc] = "D";
    }

    const bb = state.bigBoard.map((r) => [...r]);

    const userName = localStorage.getItem("userName");

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
      <main>
        <div
          id="playerOneElement"
          className={`playerElement leftPlayer ${playersEntering ? "outLeft" : ""} ${currentPlayer.character === state.rotation[0] ? "activePlayer" : ""}`}
        >
          {state.rotation[0]}
        </div>
        <div
          id="playerTwoElement"
          className={`playerElement rightPlayer ${playersEntering ? "outRight" : ""} ${currentPlayer.character === state.rotation[1] ? "activePlayer" : ""}`}
        >
          {state.rotation[1]}
        </div>
        {config.playerCount === 3 && (
          <div
            id="playerThreeElement"
            className={`playerElement rightPlayer ${playersEntering ? "outAbove" : ""} ${currentPlayer.character === state.rotation[2] ? "activePlayer" : ""}`}
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
    </>
  );
}
