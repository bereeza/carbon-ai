# carbon ingestion service

Spring Boot 3.4 service (Java 21) that exposes a REST endpoint to accept plain text and publish it as an event to a Kafka topic named `raw-content` using Spring Cloud Stream with the Kafka binder. The web server is configured to use Java virtual threads.

## Tech stack

- Spring Boot 3.4
- Java 21
- Spring Web
- Spring Cloud Stream + Kafka binder
- Lombok
