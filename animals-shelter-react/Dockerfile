FROM node:18-alpine

WORKDIR /app

COPY package.json /app/package.json
COPY webpack.config.js /app/webpack.config.js

COPY . .

RUN npm install --force

RUN npm run build --force

EXPOSE 3000

CMD [ "npm", "run", "start" ]