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
        // re-subscribe after reconnect
        for (const [dest, { callback }] of subsRef.current.entries()) {
          const sub = client.subscribe(dest, callback);
          subsRef.current.set(dest, { callback, sub });
        }
      },
    });

    client.activate();
    clientRef.current = client;
  }, []);

  const subscribe = useCallback((destination, callback) => {
    if (!clientRef.current) return;

    // already subscribed to this destination
    if (subsRef.current.has(destination)) {
      return subsRef.current.get(destination).sub;
    }

    const sub = clientRef.current.subscribe(destination, callback);
    subsRef.current.set(destination, { callback, sub });
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