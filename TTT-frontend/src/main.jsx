import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { WebSocketProvider } from "./state/WebSocketContext.jsx";
import { NotificationProvider } from "./state/NotificationContext.jsx";
import "./index.css";
import App from "./App.jsx";


window.global ||= window;

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <WebSocketProvider>
      <NotificationProvider>
        <App />
      </NotificationProvider>
    </WebSocketProvider>
  </StrictMode>,
);
