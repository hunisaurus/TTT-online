import { useState } from "react";
import { useAudio } from "../hooks/useAudio";
import Login from "./Login";
import Register from "./Register";
import GameMenu from "./GameMenu";
import Game from "./game/Game";

export default function Home() {
  const { play } = useAudio();
  const [step, setStep] = useState("intro"); // intro | login | menu | game
  const [leaving, setLeaving] = useState(false);
  const [entering, setEntering] = useState(false);
  const [authView, setAuthView] = useState("login"); // 'login' | 'register'
  const [authEntering, setAuthEntering] = useState(false);
  const [gameConfig, setGameConfig] = useState(null);

  const go = (next) => {
    setStep(next);
    setEntering(true);
    setTimeout(() => setEntering(false), 30);
    if (next === "login") {
      setAuthView("login");
      setAuthEntering(true);
      setTimeout(() => setAuthEntering(false), 30);
    }
  };

  return (
    <main>
      <h1
        id="title"
        className={`title titlescreen ${step === "intro" ? "begin" : ""} ${leaving ? "outAbove" : ""} ${step === "login" ? "titleAuth" : ""} ${step === "login" && authView === "register" ? "outAbove" : ""}`}
        onClick={() => {
          if (step === "intro") {
            play("click");
            go("login");
          }
        }}
      >
        T T T
      </h1>

      {step === "login" && (
        <>
          <Login
            className={[
              authView !== "login" ? "slideToBelow" : "",
              authView === "login" && authEntering ? "slideFromBelow" : "",
            ]
              .join(" ")
              .trim()}
            style={{ pointerEvents: authView === "login" ? undefined : "none" }}
            onSubmit={() => {
              play("click");
              go("menu");
            }}
            onRegister={() => {
              play("click");
              setAuthView("register");
              setAuthEntering(true);
              setTimeout(() => setAuthEntering(false), 30);
            }}
          />
          <Register
            className={[
              authView !== "register" ? "slideToBelow" : "",
              authView === "register" && authEntering ? "slideFromBelow" : "",
            ]
              .join(" ")
              .trim()}
            style={{
              pointerEvents: authView === "register" ? undefined : "none",
            }}
            onBack={() => {
              play("click");
              setAuthView("login");
              setAuthEntering(true);
              setTimeout(() => setAuthEntering(false), 30);
            }}
            onSubmit={() => {
              play("click");
              go("menu");
            }}
          />
        </>
      )}
      {step === "menu" && (
        <GameMenu
          onStart={(cfg) => {
            setGameConfig(cfg);
            setStep("game");
          }}
        />
      )}
      {step === "game" && gameConfig && (
        <Game
          config={gameConfig}
          onExit={() => {
            setGameConfig(null);
            setStep("menu");
          }}
        />
      )}
    </main>
  );
}
