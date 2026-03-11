import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { WebSocketProvider } from "./state/WebSocketContext.jsx";
import { NotificationProvider } from "./state/NotificationContext.jsx";
import App from "./App.jsx";
import { UserProvider } from "./state/UserContext";


window.global ||= window;

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <WebSocketProvider>
      <NotificationProvider>
          <UserProvider>
        <App />
          </UserProvider>
      </NotificationProvider>
    </WebSocketProvider>
  </StrictMode>,
);
