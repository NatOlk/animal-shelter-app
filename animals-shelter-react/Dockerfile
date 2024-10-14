# Use an official Node runtime as a parent image
FROM node:18-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the package.json and package-lock.json to the working directory
#COPY package-lock.json /app/package-lock.json
COPY package.json /app/package.json
COPY webpack.config.js /app/webpack.config.js

# Copy the remaining application files to the working directory
COPY . .

# Install the dependencies
RUN npm install --force

# Build the application
RUN npm run build --force

# Expose port 3000 for the application
EXPOSE 3000

# Start the application
CMD [ "npm", "run", "start" ]