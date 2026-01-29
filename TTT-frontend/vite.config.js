import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
// Use env to configure dev server proxy to your backend.
// Set VITE_API_URL in .env (e.g., http://localhost:8080) to change the proxy target.
export default ({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const apiTarget = env.VITE_API_URL || 'http://localhost:8080'
  return defineConfig({
    plugins: [react()],
    server: {
      proxy: {
        '/api': {
          target: apiTarget,
          changeOrigin: true,
        },
        // Proxy backend user endpoints
        '/user': {
          target: apiTarget,
          changeOrigin: true,
        },
      },
    },
  })
}
