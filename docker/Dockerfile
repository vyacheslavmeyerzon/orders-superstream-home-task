# Use the official Golang image with Alpine Linux for building the application
FROM golang:1.21-alpine AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the go.mod and go.sum files into the container
COPY ../orders-consumer-qa-home-task/go.mod ../orders-consumer-qa-home-task/go.sum ./

# Download the Go module dependencies
RUN go mod tidy

# Copy the rest of the application source code into the container
COPY ../orders-consumer-qa-home-task ./

# Build the Go application and output the binary named "orders-consumer-qa-home-task"
RUN go build -o orders-consumer-qa-home-task .

# Use the official Alpine Linux image for the final container
FROM alpine:3.20.0

# Set the working directory in the container
WORKDIR /root/

# Copy the built binary from the builder stage into the final container
COPY --from=builder /app/orders-consumer-qa-home-task .

# Install additional utilities in the container
RUN apk add --no-cache busybox-extras

# Define the command to run the application with a delay of 30 seconds
CMD ["sh", "-c", "sleep 30 && ./orders-consumer-qa-home-task"]


#############################################################################################################################
#############################################################################################################################
########################## CMD TESTING REQUEST ##############################################################################

#docker-compose up -d zookeeper kafka
#docker-compose logs -f zookeeper kafka
#docker-compose logs kafka
#docker-compose run --rm order-processor sh -c "apk add --no-cache busybox-extras && telnet kafka 9092"
#docker-compose build
#docker-compose up -d
#docker-compose exec order-processor sh
#docker-compose exec kafka kafka-console-producer --broker-list kafka:9092 --topic processed-orders
#{"orderId": "10001", "customerName": "Ploni Almoni", "orderDate": "2024-05-27", "items": [{"itemId": "item1", "quantity": 2}]}
#docker-compose exec kafka kafka-console-consumer --bootstrap-server kafka:9092 --topic processed-orders --from-beginning
#docker-compose exec order-processor kafka-console-producer --broker-list kafka:9092 --topic test-topic
#docker-compose logs kafka
#docker-compose exec kafka kafka-topics --bootstrap-server kafka:9092 --delete --topic processed-orders
#docker-compose exec kafka kafka-topics --bootstrap-server kafka:9092 --create --topic processed-orders --partitions 1 --replication-factor 1