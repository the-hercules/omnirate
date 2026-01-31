# OmniRate

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

A stateless, distributed rate-limiting service designed to be consumed by API gateways or edge services. OmniRate enforces token-bucket based limits using Redis and Lua for correctness under high concurrency. It is not a gateway itself and does not proxy requests.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
  - [API Endpoint](#api-endpoint)
  - [Request Format](#request-format)
  - [Response Format](#response-format)
  - [Example Requests](#example-requests)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Performance](#performance)
- [License](#license)

## Features

- **Token Bucket Algorithm**: Precise rate limiting using the token bucket algorithm
- **Distributed Architecture**: Stateless design allows horizontal scaling
- **Atomic Operations**: Redis + Lua scripts ensure correctness under concurrent access
- **Virtual Threads**: Leverages Project Loom for improved throughput
- **Configurable Limits**: Dynamic rate limits per client key
- **RESTful API**: Simple HTTP interface for integration with existing services
- **Production Ready**: Built with Spring Boot 4.0.1 and Actuator for monitoring

## Architecture

OmniRate uses a token bucket algorithm implemented as a Lua script executed atomically in Redis:

1. **Stateless Service**: No local state, all rate limiting data stored in Redis
2. **Lua Script Execution**: Atomic token bucket operations prevent race conditions
3. **Key-based Isolation**: Each client/resource gets independent rate limit tracking
4. **Auto-expiration**: Keys automatically expire to prevent memory leaks

### Components

- **Controller Layer**: REST API endpoint for rate limit checks
- **Service Layer**: Business logic for rate limit evaluation
- **Redis Layer**: Atomic Lua script execution for distributed consistency
- **Model Layer**: Request/Response DTOs with validation

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Redis 6.0+ (running on localhost:6379 or configured endpoint)

## Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/the-hercules/omnirate.git
   cd omnirate
   ```

2. **Build the project**:

   ```bash
   ./mvnw clean package
   ```

3. **Ensure Redis is running**:
   ```bash
   redis-server
   ```

## Configuration

Edit [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
# Application name
spring.application.name=OmniRate

# Redis configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379

# Enable virtual threads (Project Loom)
spring.threads.virtual.enabled=true
```

## Usage

### API Endpoint

**POST** `/rate-limit/check`

Checks if a request is allowed based on the specified rate limit.

### Request Format

```json
{
  "key": "user:123",
  "capacity": 100,
  "refillRate": 10
}
```

**Fields**:

- `key` (string, required): Unique identifier for the client/resource (e.g., user ID, API key, IP address)
- `capacity` (long, required, positive): Maximum number of tokens in the bucket
- `refillRate` (double, required, positive): Tokens added per second

### Response Format

**Success (200 OK)**:

```json
{
  "allowed": true,
  "remainingTokens": 99,
  "retryAfterMs": 0
}
```

**Rate Limited (429 Too Many Requests)**:

```json
{
  "allowed": false,
  "remainingTokens": 0,
  "retryAfterMs": 100
}
```

**Fields**:

- `allowed` (boolean): Whether the request should be allowed
- `remainingTokens` (long): Number of tokens remaining in the bucket
- `retryAfterMs` (long): Milliseconds to wait before retrying (0 if allowed)

### Example Requests

**Using cURL**:

```bash
curl -X POST http://localhost:8080/rate-limit/check \
  -H "Content-Type: application/json" \
  -d '{
    "key": "user:123",
    "capacity": 10,
    "refillRate": 1
  }'
```

**Using HTTPie**:

```bash
http POST :8080/rate-limit/check \
  key=user:123 \
  capacity:=10 \
  refillRate:=1
```

## Running the Application

### Using Maven

```bash
./mvnw spring-boot:run
```

### Using Java

```bash
java -jar target/OmniRate-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`.

### Health Check

Spring Boot Actuator endpoints are available:

```bash
curl http://localhost:8080/actuator/health
```

## Testing

### Unit Tests

Run the test suite:

```bash
./mvnw test
```

### Load Testing

A k6 load test script is included in [`k6-test.js`](k6-test.js):

```bash
# Install k6: https://k6.io/docs/getting-started/installation/
k6 run k6-test.js
```

This simulates concurrent requests to validate rate limiting behavior under load.

## Performance

- **Virtual Threads**: Utilizes Project Loom for efficient concurrent request handling
- **Atomic Operations**: Lua scripts ensure O(1) complexity for rate limit checks
- **Scalability**: Stateless architecture allows horizontal scaling
- **Redis Performance**: Sub-millisecond latency for rate limit checks

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
