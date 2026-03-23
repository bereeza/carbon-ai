# Carbon AI

A sophisticated microservices-based AI-powered content processing and semantic search platform built with modern Java technologies. Carbon AI enables intelligent content ingestion, AI-driven summarization, vector-based semantic search, and comprehensive analytics.

## Overview

Carbon AI is a distributed system that processes text content through an event-driven architecture. The platform ingests raw text, uses AI to generate intelligent summaries, creates vector embeddings for semantic search, and provides powerful analytics capabilities. It's designed to handle high-throughput content processing with intelligent rate limiting and scalable architecture.

## Architecture

The system consists of four main modules:

- **carbon-ingestion-service**: REST API for content ingestion with rate limiting
- **carbon-ai-service**: AI processing service for content analysis and semantic search
- **carbon-shared-library**: Common DTOs, events, and utilities
- **carbon-bom**: Bill of Materials for dependency management

### Data Flow

1. **Content Ingestion**: Raw text is submitted to the ingestion service via REST API
2. **Event Publishing**: Content is published as events to Kafka topic `raw-content`
3. **AI Processing**: AI service consumes events, generates summaries and embeddings
4. **Vector Storage**: Processed content with embeddings stored in PostgreSQL with pgvector
5. **Semantic Search**: Users can search content using natural language queries
6. **Analytics**: Search patterns and insights are tracked and analyzed

## Tech Stack

### Core Technologies
- **Java 21** with Virtual Threads for high concurrency
- **Spring Boot 3.4**
- **Spring Cloud Stream** for event-driven architecture
- **Apache Kafka** for message streaming
- **Maven** for build management and dependency management

### AI & Machine Learning
- **Spring AI 1.0.0-M6** with OpenAI integration
- **Vector Embeddings** for semantic search
- **Natural Language Processing** for content summarization

### Database & Storage
- **PostgreSQL** with **pgvector** extension for vector similarity search
- **Redis** for caching and rate limiting
- **Hibernate ORM** with vector type support
- **Flyway** for database migrations

### API & Documentation
- **Spring Web** for REST APIs
- **SpringDoc OpenAPI 3** with Swagger UI
- **Jakarta Validation** for request validation
- **MapStruct** for DTO mapping

### Development & Operations
- **Lombok** for reducing boilerplate code
- **Docker Compose** for local development environment
- **JUnit 5** for testing
- **H2** for in-memory testing

## Features

### Content Ingestion Service
- RESTful API for text content submission
- Redis-based rate limiting with token bucket algorithm
- Event-driven publishing to Kafka
- Comprehensive error handling and validation
- Swagger/OpenAPI documentation

### AI Processing Service
- Automated content summarization using AI models
- Vector embedding generation for semantic search
- Asynchronous processing with virtual threads
- Content similarity search using pgvector
- Search analytics and insights

### Shared Library
- Common data transfer objects
- Event definitions for inter-service communication
- Centralized dependency management

## Quick Start

### Prerequisites
- Java 21 or later
- Maven 3.8+
- Docker and Docker Compose

### Running the Application

1. **Start Infrastructure Services**:
   ```bash
   docker-compose up -d
   ```

2. **Build and Run Services**:
   ```bash
   # Build all modules
   mvn clean install
   
   # Run ingestion service (port 8080)
   cd carbon-ingestion-service
   mvn spring-boot:run
   
   # Run AI service (port 8081)
   cd carbon-ai-service
   mvn spring-boot:run
   ```

### API Endpoints

#### Ingestion Service (http://localhost:8080)
- `POST /api/content` - Submit content for processing
- Swagger UI available at `/swagger-ui.html`

#### AI Service (http://localhost:8081)
- `POST /api/search` - Semantic search with natural language
- `GET /api/analytics/insights` - System analytics and insights
- `GET /api/analytics/frequent-words` - Top frequent search terms
- Swagger UI available at `/swagger-ui.html`

### Example Usage

1. **Ingest Content**:
   ```bash
   curl -X POST http://localhost:8080/api/content \
     -H "Content-Type: application/json" \
     -d '{"content": "Artificial Intelligence is transforming how we interact with technology. Machine learning algorithms enable computers to learn from data and make predictions."}'
   ```

2. **Semantic Search**:
   ```bash
   curl -X POST http://localhost:8081/api/search \
     -H "Content-Type: application/json" \
     -d '{"query": "How does AI learn from data?"}'
   ```

## Configuration

### Environment Variables

#### Ingestion Service
- `SERVER_PORT`: Server port (default: 8080)
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka bootstrap servers
- `SPRING_REDIS_HOST`: Redis host
- `SPRING_REDIS_PORT`: Redis port

#### AI Service
- `SERVER_PORT`: Server port (default: 8081)
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka bootstrap servers
- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_AI_OPENAI_API_KEY`: OpenAI API key

### Rate Limiting
The ingestion service implements Redis-based rate limiting using the token bucket algorithm to prevent abuse and ensure fair usage.

## Development

### Project Structure
```
carbon-ai/
├── carbon-bom/                 # Bill of Materials
├── carbon-shared-library/      # Shared DTOs and events
├── carbon-ingestion-service/   # Content ingestion API
├── carbon-ai-service/         # AI processing and search
├── docker-compose.yml         # Development infrastructure
└── pom.xml                   # Parent POM
```

### Building
```bash
mvn clean install
```

## Monitoring and Analytics

The platform provides comprehensive analytics including:
- Search query patterns and frequency analysis
- Content processing metrics
- System performance insights
- Top frequent search terms with stop-word filtering
