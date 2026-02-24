const WebSocketContext = createContext(null);

export function WebSocketProvider({ children }) {
  const clientRef = useRef(null);

  const connect = useCallback(() => {
    if (clientRef.current) return;

    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
      reconnectDelay: 5000,
    });

    client.activate();
    clientRef.current = client;
  }, []);

  const disconnect = useCallback(() => {
    if (!clientRef.current) return;
    clientRef.current.deactivate();
    clientRef.current = null;
  }, []);

  return (
    <WebSocketContext.Provider value={{ client: clientRef.current, connect, disconnect }}>
      {children}
    </WebSocketContext.Provider>
  );
}

export function useWebSocket() {
  return useContext(WebSocketContext);
}