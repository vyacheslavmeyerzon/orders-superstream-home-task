@echo off

REM Navigate to the docker directory
cd docker

REM Build Docker containers
echo Building Docker containers...
docker-compose build

REM Start Docker containers
echo Starting Docker containers...
docker-compose up -d

REM Navigate back to the root directory
cd ..

REM Clean Maven project
echo Cleaning Maven project...
mvn clean

REM Run Maven tests
echo Running Maven tests...
mvn test

