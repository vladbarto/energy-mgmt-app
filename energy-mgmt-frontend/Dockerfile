# syntax=docker/dockerfile:1

ARG NODE_VERSION=22.9.0

# Base stage for all builds
FROM node:${NODE_VERSION}-alpine as base

# Create app directory
RUN mkdir -p /usr/src/app

# Set working directory for all stages
WORKDIR /usr/src/app

# Stage for installing dependencies (including devDependencies for build)
FROM base as deps

# Install Angular CLI globally
RUN npm install -g @angular/cli

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

# Install all dependencies (omit --omit=dev to ensure devDependencies are available for build)
RUN npm install

# Stage for building the application
FROM deps as build

# Copy the rest of the source files into the image
COPY . .

# Run the Angular build script
RUN npm run build

# Final stage to run the application with only production dependencies
FROM base as final

# Set production environment
ENV NODE_ENV=production

# Run the application as a non-root user
#USER node

# Copy all necessary files for Angular to recognize workspace
COPY --from=build /usr/src/app ./

# Expose application port
EXPOSE 4200

# Run the application
CMD ["npx", "ng", "serve", "--host", "0.0.0.0", "--port", "4200"]
#CMD ["npx", "ng", "serve", "--configuration=production", "--host", "0.0.0.0", "--port", "4200"]
