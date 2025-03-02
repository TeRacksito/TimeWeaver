FROM node:20

WORKDIR /workspace/frontend

RUN npm i -g pnpm

COPY package.json pnpm-lock.yaml ./

RUN pnpm config set store-dir /workspace/.pnpm-store && pnpm i

CMD ["pnpm", "run", "dev", "--host"]                        