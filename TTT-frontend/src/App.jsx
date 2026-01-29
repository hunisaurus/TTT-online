
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./components/Home";
import {
  makeSmallBoards,
  makeBigBoard
} from "./state/gameLogic";
import "./styles.css";

export default function App() {

  const startGame = (cfg) => {
    const smallBoards = makeSmallBoards();
    const bigBoard = makeBigBoard();
    const activeBigs = new Set([
      "0,0",
      "0,1",
      "0,2",
      "1,0",
      "1,1",
      "1,2",
      "2,0",
      "2,1",
      "2,2",
    ]);
    setConfig(cfg);
    setState({
      smallBoards,
      bigBoard,
      activeBigs,
      rotation: cfg.rotation,
      moves: [],
    });
    setBoardEntering(true);
    setPlayersEntering(true);
  };

  return (
    <main>
      <Router>
        <Routes>
          <Route
            path="/"
            element={<Home onStart={startGame}/>}
          />
        </Routes>
      </Router>
    </main>
  );
}
