import { useState } from "react";
import { useAudio } from "../hooks/useAudio";
import Login from "./Login";
import Register from "./Register";
import GameMenu from "./GameMenu";
import Game from "./game/Game";
import "../StyleCSS/global.css";
import "../StyleCSS/auth.css";
import OnlineGame from "./game/OnlineGame";

export default function Home() {
    const {play} = useAudio();
    const [step, setStep] = useState("intro");
    const [leaving, setLeaving] = useState(false);
    const [entering, setEntering] = useState(false);
    const [authView, setAuthView] = useState("login");
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
        <>
            {step === "menu" && (
                <header className="game-header">
                    <div className="header-content">
                        <div className="brand">
                            <span className="logo-dot"></span>
                            <span className="logo-text">TTT ONLINE</span>
                        </div>
                        <div className="user-profile-badge">
                            <div className="user-info">
                                <span className="user-label">LOGGED IN AS</span>
                                <span className="user-name">{localStorage.getItem('userName') || 'Guest'}</span>
                            </div>
                            <div className="user-avatar">
                                {localStorage.getItem('userName')?.charAt(0).toUpperCase() || 'P'}
                            </div>
                        </div>
                    </div>
                </header>
            )}

            <main className={`app-container ${step === "menu" ? "with-header" : ""}`}>
                {step !== "game" && (
                    <div className="title-wrapper">
                        <h1
                            className={`title-display 
                    ${step === "intro" ? "pulse-animation" : "auth-mode"} 
                    ${leaving ? "fade-out-up" : ""}`}
                            onClick={() => {
                                if (step === "intro") {
                                    play("click");
                                    go("login");
                                }
                            }}
                        >
                            T T T
                        </h1>
                    </div>
                )}

                <div className="content-area">
                    {step === "login" && (
                        <div className="auth-view-container">
                            {authView === "login" ? (
                                <Login
                                    className={authEntering ? "fade-in-bottom" : ""}
                                    onSubmit={() => go("menu")}
                                    onRegister={() => setAuthView("register")}
                                />
                            ) : (
                                <Register
                                    className={authEntering ? "fade-in-bottom" : ""}
                                    onBack={() => setAuthView("login")}
                                    onSubmit={() => go("menu")}
                                />
                            )}
                        </div>
                    )}

                    {step === "menu" && (
                        <div className="menu-container-fade-in">
                            <GameMenu onStart={(cfg) => { setGameConfig(cfg); setStep("game"); }} />
                        </div>
                    )}

        {step === "game" &&
          gameConfig &&
          (gameConfig.mode == "online" ? (
            <OnlineGame
              config={gameConfig}
              setStep={setStep}
              onExit={() => {
                setStep("menu");
              }}
            />
          ) : (
            <Game
              config={gameConfig}
              setStep={setStep}
              onExit={() => {
                setGameConfig(null);
                setStep("menu");
              }}
            />
          ))}
            </div>
      </main>
    </>
  );
}