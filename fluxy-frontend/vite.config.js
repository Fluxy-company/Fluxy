import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')

  return {
    plugins: [react()],
    define: {
      __APP_VERSION__: JSON.stringify(env.npm_package_version),
    },
    server: {
      host: '0.0.0.0',
      port: parseInt(env.VITE_APP_PORT),
    },
  }
})
