FROM node:18-alpine as build

WORKDIR /app

COPY package.json /app/package.json
COPY webpack.config.js /app/webpack.config.js

COPY . .

RUN npm install --force

RUN npm run build --force

RUN npx tailwindcss -c tailwind.config.js -i ./src/styles/global.css -o ./src/styles/output.css

FROM node:18-alpine

WORKDIR /app

COPY package.json /app/package.json
COPY webpack.config.js /app/webpack.config.js

COPY . .

COPY --from=build /app/build /app/build
EXPOSE 3000

ENV HTTPS=true
ENV SSL_CRT_FILE=/app/nginx/cert.crt
ENV SSL_KEY_FILE=/app/nginx/cert.key

CMD [ "npm", "run", "start" ]