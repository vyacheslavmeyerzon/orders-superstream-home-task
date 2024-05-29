#!/bin/bash

# Navigate to the docker directory
cd docker

# Build Docker containers
echo "Building Docker containers..."
docker-compose build

# Start Docker containers
echo "Starting Docker containers..."
docker-compose up -d

# Navigate back to the root directory
cd ..

# Clean Maven project
echo "Cleaning Maven project..."
mvn clean

# Run Maven tests
echo "Running Maven tests..."
mvn test
