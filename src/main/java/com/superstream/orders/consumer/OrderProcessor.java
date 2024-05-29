package com.superstream.orders.consumer;

// Importing necessary classes from the Apache Kafka and Spring frameworks
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DestinationTopic;
import org.springframework.stereotype.Service;

// Annotation to mark this class as a Spring service component
@Service
public class OrderProcessor {

    // Creating a logger instance for logging events
    private static final Logger logger = LogManager.getLogger(OrderProcessor.class);

    // Declaring a KafkaTemplate to send messages to Kafka topics
    private final KafkaTemplate<String, String> kafkaTemplate;

    // Constructor to initialize the KafkaTemplate through dependency injection
    public OrderProcessor(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Method to listen to the "raw-orders" Kafka topic and process incoming messages
    @KafkaListener(topics = "raw-orders", groupId = "order-processors")
    public void processOrder(ConsumerRecord<String, String> record) {
        // Logging the received order message
        logger.info("Received order: {}", record.value());

        // Processing the order message (this example simply adds a status field)
        String processedOrder = record.value().replace("}", ", \"status\": \"Processed\"}");

        // Publishing the processed order message to the "processed-orders" Kafka topic
        kafkaTemplate.send(new ProducerRecord<>("processed-orders", processedOrder));

        // Logging the processed order message
        logger.info("Processed order: {}", processedOrder);
    }
}
