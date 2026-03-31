# API Gateway

A production-ready API Gateway built with Spring Boot, featuring rate limiting, circuit breaker patterns, and comprehensive request logging.

## Quick Start

### Build
```bash
./mvnw clean package
```

### Run
```bash
./mvnw spring-boot:run
```

Server starts on `http://localhost:8080`

## Features

- **Rate Limiting**: Fixed Window, Sliding Window, Token Bucket algorithms
- **Circuit Breaker**: Prevent cascading failures
- **Load Balancing**: Round Robin, Least Connections, Sticky Sessions
- **Audit Logging**: Complete request and system audit trails
- **Health Checks**: Service instance health monitoring

## Documentation

- [DEVELOPMENT.md](DEVELOPMENT.md) - Development guide and API reference
- [test-endpoints.sh](test-endpoints.sh) - Test script for all endpoints

## Requirements

- Java 21+
- Maven 3.8+

## Project Status

✅ All core features implemented and tested
✅ Lombok framework removed for explicit code
✅ Full audit and request logging
✅ Circuit breaker and rate limiting active

