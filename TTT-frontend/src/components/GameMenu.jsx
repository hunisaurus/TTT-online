import {useState} from "react";
import {useAudio} from "../hooks/useAudio";
import "../gameMenu.css"
import CreateGame from "./game/steps/CreateGame.jsx";
import ServerBrowser from "./game/steps/ServerBrowser";
import OnlineLoadList from "./game/steps/OnlineLoadList";

const CHARSET = ["◯", "✖", "△"];

export default function GameMenu({onStart}) {
    const {play} = useAudio();

    const [mode, setMode] = useState(null); // 'pvp' | 'pve'
    const [onlineType, setOnlineType] = useState(null); // 'quick' | 'browser'
    const [playerCount, setPlayerCount] = useState(2);
    const [difficulty, setDifficulty] = useState("easy"); // easy|medium|hard|unbeatable
    const [chars, setChars] = useState([]); // chosen tokens in order
    const [rotation, setRotation] = useState([]); // chosen start order (tokens)
    const [step, setStep] = useState("startMenu"); // mode | multiType | onlineType | pvpCount | pveDiff | serverBrowser | char | start | leaving
    const [leaving, setLeaving] = useState(false);
    const [entering, setEntering] = useState(false);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [currentGameId, setCurrentGameId] = useState(null);


    const requiredChars = (mode === "pvp") ? playerCount : 2;

    const go = (next) => {
        setStep(next);
        setEntering(true);
        setTimeout(() => setEntering(false), 30);
    };

    const addChar = (ch) => {
        if (chars.includes(ch)) {
            play("noclick");
            return;
        }
        if (chars.length < requiredChars) {
            play("click");
            const next = [...chars, ch];
            setChars(next);
            const needed = requiredChars;
            if (next.length === needed) {
                go("start");
            }
        } else {
            play("noclick");
        }
    };

    const finishConfigAndStart = (finalRotation) => {
        setLeaving(true);
        setTimeout(() => {
            play("gamestart");
            go("game")
            onStart({mode, playerCount, difficulty, chars, rotation: finalRotation});
        }, 400);
    };

    const addStart = (ch) => {
        play("click");
        if (!chars.includes(ch)) return;
        if (rotation.includes(ch)) return;
        const needed = mode === "pvp" ? playerCount : 2;
        if (rotation.length < needed) {
            const next = [...rotation, ch];
            if (mode === "pvp" && playerCount === 3 && next.length === 2) {
                const third = chars.find((x) => !next.includes(x));
                const finalRotation = [...next, third];
                setRotation(finalRotation);
                finishConfigAndStart(finalRotation);
            } else if (mode === "pvp" && playerCount === 2 && next.length === 1) {
                const second = chars.find((x) => x !== ch);
                const finalRotation = [ch, second];
                setRotation(finalRotation);
                finishConfigAndStart(finalRotation);
            } else if (mode === "pve" && next.length === 1) {
                const second = chars.find((x) => x !== ch);
                const finalRotation = [ch, second];
                setRotation(finalRotation);
                finishConfigAndStart(finalRotation);
            } else {
                setRotation(next);
            }
        }
    };

    const startGame = () => {
        play("gamestart");
        setStep("game");
        onStart({mode, playerCount, difficulty, chars, rotation});
    };
    const back = () => {
        play("click");

        switch (step) {
            case "mode":
                go("startMenu");
                break;
            case "multiType":
                go("mode");
                break;
            case "onlineType":
                go("multiType");
                break;
            case "pvpCount":
                go("multiType");
                break;
            case "pveDiff":
                go("mode");
                break;
            case "serverBrowser":
                go("onlineType");
                break;
            case "createGame":
                go("onlineType");
                break;
            case "quick-match":
                go("onlineType");
                break;
            case "char":
                if (mode === "pve") {
                    go("pveDiff");
                } else if (mode === "online") {
                    if (onlineType === "create") {
                        go("createGame");
                    } else if (onlineType === "quick") {
                        go("onlineType");
                    } else if (onlineType === "browser") {
                        go("serverBrowser");
                    } else {
                        go("onlineType");
                    }
                } else {
                    go("pvpCount");
                }
                break;
            case "onlineLoadList":
                go("onlineType");
                break;
            case "start":
                go("char");
                setChars([]);
                setRotation([]);
                break;
            default:
                go("startMenu");
        }
    };

    return (<main>
        <>
            <div className={`hamburger-icon ${isMenuOpen ? "open" : ""}`}
                 onClick={() => setIsMenuOpen(!isMenuOpen)}>
                <span></span>
                <span></span>
                <span></span>
            </div>

            <nav className={`side-menu ${isMenuOpen ? "active" : ""}`}>
                <ul className="menu-list">
                    <li onClick={() => {
                        play("click");
                        go("profile");
                        setIsMenuOpen(false);
                    }}>
                        <i className="icon-user"></i> PROFILE
                    </li>
                    <li onClick={() => {
                        play("click");
                        go("chat");
                        setIsMenuOpen(false);
                    }}>
                        <i className="icon-chat"></i> CHAT
                    </li>
                    <li className="logout-item" onClick={() => {
                        play("click");
                        localStorage.removeItem('userName');
                        window.location.reload();
                    }}>
                        <i className="icon-logout"></i> LOGOUT
                    </li>
                </ul>
            </nav>

            {isMenuOpen && <div className="menu-overlay" onClick={() => setIsMenuOpen(false)}></div>}

            {step === "startMenu" && (<div className="play-container">
                <button
                    className="bigPlayButton"
                    onMouseOver={() => play("hover")}
                    onClick={() => {
                        play("click");
                        go("mode");
                    }}
                >
                    PLAY
                </button>
            </div>)}
            {step === "mode" && (<>
                <h2 className={`helptext ${entering ? "outBelow" : ""}`}>
                    CHOOSE GAME MODE!
                </h2>
                <button
                    className="leftButton pve"
                    onMouseOver={() => play("hover")}
                    onClick={() => {
                        play("click");
                        setMode("pve");
                        go("pveDiff");
                    }}
                >
                    SINGLE PLAYER{"\n"}(vs CPU)
                </button>
                <button
                    className="rightButton pvp"
                    onMouseOver={() => play("hover")}
                    onClick={() => {
                        play("click");
                        go("multiType");
                    }}
                >
                    MULTIPLAYER{"\n"}(LAN/Online)
                </button>
            </>)}
            {step !== "startMenu" && step !== "leaving" && (<button className="back-button-modern" onClick={back}>
                <span className="arrow-icon">←</span>
            </button>)}

            {step === "multiType" && (<>
                <h2 className="helptext">MULTIPLAYER TYPE</h2>
                <button
                    className="leftButton"
                    onClick={() => {
                        play("click");
                        setMode("pvp");
                        go("pvpCount");

                    }}
                >
                    LAN{"\n"}(Local)
                </button>
                <button
                    className="rightButton"
                    onClick={() => {
                        play("click");
                        go("onlineType");
                    }}
                >
                    ONLINE{"\n"}(Network)
                </button>
            </>)}

            {step === "onlineType" && (<>
                <h2 className="helptext">ONLINE MATCHMAKING</h2>
                <button
                    className="leftButton"
                    onClick={() => {
                        play("click");
                        setMode("online");
                        setOnlineType("quick");
                        setPlayerCount(2);
                        go("quick-match");
                    }}
                >
                    QUICK{"\n"}MATCH
                </button>
                <button
                    className="rightButton"
                    onClick={() => {
                        play("click");
                        setMode("online");
                        setOnlineType("browser");
                        go("serverBrowser");
                    }}
                >
                    SERVER{"\n"}BROWSER
                </button>
                <button
                    className="leftButton-down"
                    onClick={() => {
                        play("click");
                        setMode("online");
                        setOnlineType("create");
                        go("createGame");
                    }}
                >
                    CREATE {"\n"} NEW GAME
                </button>
                <button
                    className="rightButton-down"
                    onClick={() => {
                        play("click");
                        setMode("online");
                        go("onlineLoadList");
                    }}
                >
                    LOAD {"\n"} GAME
                </button>
            </>)}

            {step === "pvpCount" && (<>
                <h2 className="helptext">HOW MANY PLAYERS?</h2>
                <button
                    className="leftButton"
                    onClick={() => {
                        setPlayerCount(2);
                        go("char");
                    }}
                >
                    TWO PLAYER
                </button>
                <button
                    className="rightButton"
                    onClick={() => {
                        setPlayerCount(3);
                        go("char");
                    }}
                >
                    THREE PLAYER
                </button>
            </>)}
            {step === "createGame" && (
                <CreateGame onContinue={(game) => {
                    setCurrentGameId(game.id);
                    go("char");
                }} />
            )}

            {step === "serverBrowser" && (
                <ServerBrowser onJoin={(game) => {
                    setCurrentGameId(game.id);
                    go("char");
                }} />
            )}

            {step === "onlineLoadList" && (
                <OnlineLoadList
                    currentUserId={localStorage.getItem('userId')}
                    onSelect={(game) => {
                        play("click");
                        setCurrentGameId(game.id);
                        onStart({
                            mode: "online",
                            chars: game.savedChars,
                            board: game.board_state
                        });
                        go("game");
                    }}
                />
            )}

            {step === "pveDiff" && (<>
                <h2 className={`helptext ${entering ? "outBelow" : ""}`}>
                    HOW ADEPT SHOULD THE AI BE?
                </h2>
                <div className="difficulty-container">
                    {["easy", "medium", "hard", "unbeatable"].map((d, i) => (<button
                        key={d}
                        className={["diff-btn", d === difficulty ? "clicked" : "", ["farLeft", "midLeft", "midRight", "farRight"][i],].join(" ")}
                        onClick={() => {
                            play("click");
                            setDifficulty(d);
                            go("char");
                        }}
                    >
                        {d.toUpperCase()}
                    </button>))}
                </div>
            </>)}

            {(step === "char") && (<div className="menu-step-container">
                <h2 className="helptext">
                    {mode === "pve" && chars.length === 0 ? "PICK YOUR TOKEN!" : mode === "pve" && chars.length === 1 ? "PICK THE CPU'S TOKEN!" : `PLAYER ${chars.length + 1} PICK!`}
                </h2>

                <div className="char-grid">
                    {CHARSET.filter(ch => !chars.includes(ch)).map((ch) => (<button
                        key={ch}
                        className="charChoice"
                        onMouseOver={() => play("hover")}
                        onClick={() => {
                            play("click");
                            const newChars = [...chars, ch];
                            setChars(newChars);

                            if (newChars.length === playerCount) {
                                go("start");
                            }
                        }}
                    >
                        {ch}
                    </button>))}
                </div>
            </div>)}

            {step === "start" && (<div className="start-confirm-area">
                <h2 className="helptext">WHO STARTS THE GAME?</h2>

                <div className="start-container">
                    {chars.map((ch) => (<button
                        key={ch}
                        className={`whoStarts ${rotation[0] === ch ? "active-starter" : ""}`}
                        onClick={() => {
                            play("click");
                            const others = chars.filter(c => c !== ch);
                            setRotation([ch, ...others]);
                        }}
                    >
                        {ch}
                    </button>))}
                </div>

                {rotation.length > 0 && (<div className="start-action-wrapper">
                    <button className="bigPlayButton start-phase-button" onClick={startGame}>
                        START GAME
                    </button>
                </div>)}
            </div>)}

            {step === "game" && (
                <button
                    className="game-exit-button"
                    style={{
                        zIndex: 10000,
                        position: 'fixed',
                        display: 'block',
                        visibility: 'visible',
                        opacity: 1
                    }}
                    onClick={() => {
                        play("click");
                        setChars([]);
                        setRotation([]);
                        setMode(null);
                        go("startMenu");
                    }}
                >
                    QUIT TO MENU
                </button>
            )}
        </>
    </main>);
}