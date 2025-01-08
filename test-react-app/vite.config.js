import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from "path"
import websocket from 'websocket';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      'net': 'net-browserify',
      "@": path.resolve(__dirname, "./src"),
    }
  },
  define: {
    global: 'globalThis',
  }
})