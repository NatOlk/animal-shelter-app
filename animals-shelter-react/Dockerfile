FROM node:19-alpine

WORKDIR /app

COPY package.json /app/package.json

COPY . .

RUN npm install --force

RUN npm run build

RUN npm install vite --save-dev

RUN npm ci --force

RUN npx tailwindcss -c tailwind.config.js -i ./src/styles/global.css -o ./src/styles/output.css

EXPOSE 5173

CMD [ "npm", "run", "dev", "--", "--port=5173", "--host", "0.0.0.0"]
