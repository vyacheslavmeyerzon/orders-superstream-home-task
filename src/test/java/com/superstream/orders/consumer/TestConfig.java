package com.superstream.orders.consumer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

/**
 * This configuration class sets up an embedded Kafka broker for testing purposes and provides
 * a KafkaTemplate bean for producing messages to Kafka topics.
 */
@Configuration
@EmbeddedKafka(partitions = 1, topics = {"raw-orders", "processed-orders"})
public class TestConfig {

    /**
     * Creates and configures an EmbeddedKafkaBroker bean with specific settings.
     *
     * @return the configured EmbeddedKafkaBroker
     */
    @Bean
    public EmbeddedKafkaBroker embeddedKafkaBroker() {
        // Creates an embedded Kafka broker with 3 brokers in the cluster,
        // and 3 partitions for the specified topics
        return new EmbeddedKafkaBroker(3, true, 3, "raw-orders", "processed-orders");
    }

    /**
     * Creates and configures a KafkaTemplate bean using the embedded Kafka broker.
     *
     * @param embeddedKafka the EmbeddedKafkaBroker to use
     * @return the configured KafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(EmbeddedKafkaBroker embeddedKafka) {
        // Retrieves producer properties for the embedded Kafka broker
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafka);
        // Sets the key and value serializer classes for the producer
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Creates a producer factory with the configured properties
        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        // Returns a new KafkaTemplate with the created producer factory
        return new KafkaTemplate<>(producerFactory);
    }
}
