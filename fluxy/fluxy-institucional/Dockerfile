# ---- Stage 1: instala dependências ----
FROM node:22-alpine AS deps

WORKDIR /app

COPY package.json package-lock.json* yarn.lock* pnpm-lock.yaml* ./
RUN \
    if [ -f yarn.lock ]; then yarn install --frozen-lockfile; \
    elif [ -f pnpm-lock.yaml ]; then npm i -g pnpm && pnpm i --frozen-lockfile; \
    else npm install; \
    fi

# ---- Stage 2: dev (HMR via Vite) ----
FROM deps AS dev

WORKDIR /app

# O código-fonte vem via volume no compose
COPY . .

EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]

# ---- Stage 3: build de produção ----
FROM deps AS builder

WORKDIR /app
COPY . .
RUN npm run build

# ---- Stage 4: prod (nginx) ----
FROM nginx:stable-alpine AS prod

COPY --from=builder /app/dist /usr/share/nginx/html

# Configuração para SPA (React Router)
RUN echo 'server { \
    listen 80; \
    root /usr/share/nginx/html; \
    index index.html; \
    location / { try_files $uri $uri/ /index.html; } \
    }' > /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]