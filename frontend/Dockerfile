FROM node:20

WORKDIR /workspace/frontend

RUN npm i -g pnpm

COPY package.json pnpm-lock.yaml ./

RUN pnpm config set store-dir /workspace/.pnpm-store
RUN pnpm i
RUN /bin/bash -c "SHELL=/bin/bash pnpm setup && source /root/.bashrc && pnpm install -g @angular/cli"

CMD ["pnpm", "run", "start", "--host", "0.0.0.0", "--port", "3000"]
