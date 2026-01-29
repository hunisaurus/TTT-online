
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Game from "./components/game/Game";
import "./styles.css";

export default function App() {
  return (
    <main>
      <Router>
        <Routes>
          <Route path="/" element={<Game />} />
        </Routes>
      </Router>
    </main>
  );
}
