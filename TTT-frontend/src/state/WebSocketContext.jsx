import { createContext, useCallback, useContext} from "react";

const WebSocketContext = createContext(null);

export function WebSocketProvider({ children }) {
  const clientRef = useRef(null);
  const subsRef = useRef(new Map()); // dest -> subscription

  const connect = useCallback(() => {
    if (clientRef.current) return;

    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
      reconnectDelay: 5000,
      onConnect: () => {
        for (const [dest, entry] of subsRef.current.entries()) {
          const sub = client.subscribe(dest, entry.callback);
          subsRef.current.set(dest, { ...entry, sub });
        }
      },
    });

    client.activate();
    clientRef.current = client;
  }, []);

  const subscribe = useCallback((destination, callback) => {
    if (!clientRef.current) return;
    const existing = subsRef.current.get(destination);
    if (existing) return existing.sub;

    const entry = { callback, sub: null };
    subsRef.current.set(destination, entry);

    // if not connected yet, just store and let onConnect do the real subscribe
    if (!clientRef.current || !clientRef.current.connected) {
      return null;
    }

    const sub = clientRef.current.subscribe(destination, callback);
    entry.sub = sub;
    return sub;
  }, []);

  const disconnect = useCallback(() => {
    if (!clientRef.current) return;
    for (const { sub } of subsRef.current.values()) {
      sub.unsubscribe();
    }
    subsRef.current.clear();
    clientRef.current.deactivate();
    clientRef.current = null;
  }, []);

  return (
    <WebSocketContext.Provider
      value={{ client: clientRef.current, connect, subscribe, disconnect }}
    >
      {children}
    </WebSocketContext.Provider>
  );
}

export function useWebSocket() {
  return useContext(WebSocketContext);
}
