# Dockerfile

# Use Node.js 18.18.0 to build and run the app
FROM node:20.18.0-alpine

# Set working directory in the container
WORKDIR /app

# Copy package files to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Expose port 3000
EXPOSE 3000

# Start the React application
CMD ["npm", "start"]