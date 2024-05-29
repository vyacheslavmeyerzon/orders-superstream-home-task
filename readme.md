# Order Process Home Task

## Overview - Kafka Consumer Microservice Testing Framework
This project aims to create an automated test framework for a microservice that consumes messages from a Kafka topic, processes them, and then publishes results to another Kafka topic. 

## Prerequisites
To run this application, you will need:
- Java (version 17 or higher)
- Docker (for running Kafka)

## Project Structure
```css
.
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── superstream
│   │               └── orders
│   │                   └── consumer
│   │                       ├── Application.java
│   │                       └── OrderProcessor.java
│   ├── test
│   │   └── java
│   │       └── com
│   │           └── superstream
│   │               └── orders
│   │                   └── consumer
│   │                       ├── OrderProcessorTest.java
│   │                       └── TestConfig.java
├── docker
│   ├── Dockerfile
│   └── docker-compose.yml
├── README.md
└── pom.xml
```


## How to Run

### Step 1: Clone the Project Repository:
```bash
git clone <repository-url>
cd <repository-directory>
```

### Step 2: Clone Go microservice Repository:
```bash
git clone https://github.com/superstreamlabs/orders-consumer-qa-home-task.git
cd orders-consumer-qa-home-task
```

### Step 3: Setup Docker and Kafka
You can set up Kafka locally using Docker.

### Step 3: Run the application locally
```bash
mvn clean install
```

```bash
cd <repository-directory>
docker-compose build
docker-compose up -d
mvn clean
mvn test

```

### Step 3.1 Run the application using Bat file
```bash
cd <repository-directory>
./run_all.bat
```

### Step 4 Debug and validation if needed
```bash
docker-compose up -d zookeeper kafka

docker-compose logs -f zookeeper kafka

docker-compose logs kafka

docker-compose run --rm order-processor sh -c "apk add --no-cache busybox-extras && telnet kafka 9092"

docker-compose build
docker-compose up -d

docker-compose exec order-processor sh

docker-compose exec kafka kafka-console-producer --broker-list kafka:9092 --topic processed-orders
{"orderId": "10001", "customerName": "Ploni Almoni", "orderDate": "2024-05-27", "items": [{"itemId": "item1", "quantity": 2}]}
docker-compose exec kafka kafka-console-consumer --bootstrap-server kafka:9092 --topic processed-orders --from-beginning
docker-compose exec order-processor kafka-console-producer --broker-list kafka:9092 --topic test-topic

docker-compose logs kafka

docker-compose exec kafka kafka-topics --bootstrap-server kafka:9092 --delete --topic processed-orders

docker-compose exec kafka kafka-topics --bootstrap-server kafka:9092 --create --topic processed-orders --partitions 1 --replication-factor 1
```

### Usage
After launch, wait for installation and testing run.
### Configuration
Make sure that your version settings, Maven, Spring, Kafka, Go, github.com/IBM/sarama do not conflict.
Based folloeing ticket: https://github.com/IBM/sarama/issues/2891
```text
In Kafka 1.40.x and older the default value if you didn't have a pinned kafka version was 1.0.0.0 protocol. 
In Sarama 1.41.x and newer we bumped the default to 2.1.0.0 protocol as per the Changelog https://github.com/IBM/sarama/releases/tag/v1.41.0 — this was at the request of the Kafka maintainers
who are looking to remove support for older protocol versions.

That's why the problem was resolved both by rolling back client version (then rolling back to default 1.x.x.x protocol) or by rolling forward kafka version (because it then supported protocol 2.1.0.0 fine).
```