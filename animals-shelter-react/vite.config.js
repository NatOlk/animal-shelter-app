import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react({
     include: "**/*.jsx",
   })],
  host: true,
  server: {
    host: '0.0.0.0',
    port: 5173,
    allowedHosts: ['localhost', 'ansh-react-app', '0.0.0.0'],
    watch: {
      usePolling: true,
      include: '**/*.jsx'
    },
  },
});
