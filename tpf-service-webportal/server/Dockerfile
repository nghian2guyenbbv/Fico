FROM node:10
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
ENTRYPOINT [ "NODE_ENV=dev", "node", "src/main.js" ]