# API Gateway

A Spring Boot-based API Gateway application with rate limiting, circuit breaker, and request logging capabilities.

## Features

- **Rate Limiting**: Multiple rate limiting algorithms (Fixed Window, Sliding Window, Token Bucket)
- **Circuit Breaker Pattern**: Prevent cascading failures with resilience4j
- **Request/Response Logging**: Comprehensive audit logging and request tracking
- **Load Balancing**: Multiple strategies including Round Robin, Least Connections, and Sticky Sessions
- **Service Discovery**: Support for multiple service instances with health checks

## Technology Stack

- Java 21
- Spring Boot 4.0.3
- Spring Cloud 2025.1.0
- Hibernate 7.2.4.Final
- H2 Database (in-memory, can be configured for PostgreSQL)
- Resilience4j for circuit breaker pattern

## Build and Run

### Prerequisites
- JDK 21+
- Maven 3.8+

### Building
```bash
./mvnw clean package
```

### Running
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing

Run the endpoint tests:
```bash
bash test-endpoints.sh
```

## Project Structure

```
src/main/java/com/apigateway/
├── controller/      # REST API endpoints
├── service/         # Business logic
├── model/           # JPA entities
├── dto/             # Data Transfer Objects
├── repository/      # Database access layer
├── filter/          # Request filters
├── ratelimit/       # Rate limiting algorithms
└── config/          # Configuration classes
```

## Configuration

See `application.properties` for application configuration including:
- Server port
- Database settings
- Logging levels
- Resilience4j settings

## API Endpoints

### Audit Logs
- `GET /api/logs/audit/{id}` - Get audit log by ID
- `GET /api/logs/audit/entity-history` - Get entity change history
- `GET /api/logs/audit/by-action/{action}` - Get changes by action type
- `GET /api/logs/audit/summary` - Get audit summary

### Request Logs
- `GET /api/logs/requests/{id}` - Get request log by ID
- `GET /api/logs/requests/user/{userId}` - Get requests by user
- `GET /api/logs/requests/errors` - Get error requests
- `GET /api/logs/requests/slow` - Get slow requests
- `GET /api/logs/requests/status-distribution` - Get status code distribution

### Rate Limit Policies
- `POST /api/rate-limit-policies` - Create policy
- `GET /api/rate-limit-policies/{id}` - Get policy by ID
- `PUT /api/rate-limit-policies/{id}` - Update policy
- `DELETE /api/rate-limit-policies/{id}` - Delete policy

### Circuit Breaker
- `GET /circuit-breaker/state/{serviceName}` - Get circuit breaker state
- `POST /circuit-breaker/failure/{serviceName}` - Record failure
- `POST /circuit-breaker/success/{serviceName}` - Record success
- `GET /circuit-breaker/allow/{serviceName}` - Check if request allowed

## Development

### Code Style
- All Lombok annotations have been removed in favor of manual implementations
- Use explicit constructor injection via `@Autowired`
- Follow Spring naming conventions

### Adding New Features
1. Create model class in `model/`
2. Create repository interface extending JpaRepository in `repository/`
3. Create DTO in `dto/` (if needed)
4. Create service class in `service/`
5. Create controller class in `controller/`

## License

Proprietary - All Rights Reserved
