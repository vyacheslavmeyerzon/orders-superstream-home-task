package com.superstream.orders.consumer;

// Importing necessary classes from the Spring framework
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Annotation to mark this class as the main entry point of a Spring Boot application
@SpringBootApplication
public class Application {

    // Main method that serves as the entry point of the Java application
    public static void main(String[] args) {
        // SpringApplication.run() method to launch the application
        SpringApplication.run(Application.class, args);
    }
}
