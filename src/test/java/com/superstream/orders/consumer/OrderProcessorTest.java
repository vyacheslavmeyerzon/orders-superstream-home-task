package com.superstream.orders.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertTrue;

/**
 * This class contains unit tests for the OrderProcessor service, using an embedded Kafka broker.
 */
@EmbeddedKafka(partitions = 1, topics = {"raw-orders", "processed-orders"})
@ContextConfiguration(classes = {OrderProcessor.class, TestConfig.class})
public class OrderProcessorTest extends AbstractTestNGSpringContextTests {

    // Uncomment to enable logging for this test class
    private static final Logger logger = LogManager.getLogger(OrderProcessorTest.class);

    // Autowired a KafkaTemplate to send messages to Kafka topics
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Autowires an embedded Kafka broker for testing purposes
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    // Kafka consumer to read messages from Kafka topics
    private KafkaConsumer<String, String> consumer;

    /**
     * Sets up the Kafka consumer with the necessary configurations before running tests.
     */
    @BeforeClass
    public void setUp() {
        // Retrieves consumer properties for the embedded Kafka broker
        Map<String, Object> consumerProps = new HashMap<>(KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka));
        // Sets the key and value deserializer classes for the consumer
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // Initializes the Kafka consumer with the configured properties
        consumer = new KafkaConsumer<>(consumerProps);
        // Subscribes the consumer to the "processed-orders" topic
        consumer.subscribe(Arrays.asList("processed-orders"));
    }

    /**
     * Tests the order processing by sending a raw order to the "raw-orders" topic and
     * verifying the processed order in the "processed-orders" topic.
     * @throws InterruptedException if the thread is interrupted while waiting for the message
     */
    @Test
    public void testOrderProcessing() throws InterruptedException {
        // Sample raw order JSON string
        String rawOrder = "{\"orderId\": \"10001\", \"customerName\": \"Ploni Almoni\", \"orderDate\": \"2024-05-27\", \"items\": [{\"itemId\": \"item1\", \"quantity\": 2}]}";
        // Sends the raw order to the "raw-orders" topic
        kafkaTemplate.send(new ProducerRecord<>("raw-orders", rawOrder));
        // Uncomment to enable logging for sent raw orders
        logger.info("Sent raw order: {}", rawOrder);

        // Waits for the processed order to be available in the "processed-orders" topic with a 30-second timeout
        ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "processed-orders", Duration.ofSeconds(30));
        // Uncomment to enable logging for received processed orders
        logger.info("Received processed order: {}", record.value());

        // Prints the value of the received ConsumerRecord to the console
        System.out.println("ConsumerRecord Value: " + record.value());

        // Asserts that the processed order contains the customer name "Ploni Almoni"
        assertTrue(record.value().contains("Ploni Almoni"));
    }
}
