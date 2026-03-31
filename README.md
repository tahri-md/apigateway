# API Gateway

A comprehensive Spring Boot-based API Gateway with request logging, audit tracking, circuit breaker patterns, rate limiting, and JWT authentication.

## Features

### Core Functionality
- ✅ **Request Logging** - Track all incoming requests with details (IP, method, path, response time)
- ✅ **Audit Logging** - Maintain audit trails for all entity changes
- ✅ **Circuit Breaker** - Prevent cascading failures with circuit breaker pattern
- ✅ **Rate Limiting** - Multiple rate limiting algorithms (Fixed Window, Sliding Window, Token Bucket)
- ✅ **Service Instance Management** - Dynamic service instance registration and health tracking
- ✅ **Load Balancing** - Multiple strategies (Round Robin, Least Connections, Sticky Session)

### Security
- ✅ **JWT Authentication** - Token-based authentication with bearer tokens
- ✅ **Role-Based Authorization** - ROLE_ADMIN, ROLE_GATEWAY support
- ✅ **CORS Configuration** - Cross-Origin Resource Sharing support
- ✅ **Input Validation** - Request validation with @Valid annotations

### Production Ready
- ✅ **Docker Support** - Dockerfile and docker-compose for containerized deployment
- ✅ **Database Profiles** - Support for H2 (dev), PostgreSQL & MySQL (prod)
- ✅ **Unit Tests** - Service layer unit tests with Mockito
- ✅ **Integration Tests** - End-to-end API testing
- ✅ **Health Checks** - Built-in `/actuator/health` endpoints with custom indicators
- ✅ **Metrics & Monitoring** - Prometheus metrics at `/actuator/prometheus`
- ✅ **Distributed Tracing** - Spring Cloud Sleuth integration with Jaeger support
- ✅ **Structured Logging** - JSON-formatted logs with Logback, per-module configuration
- ✅ **Error Handling** - Centralized exception handling with consistent error responses

### Advanced Features (Latest)
- ✅ **Rate Limiting Enforcement** - Active rate limit checking with request blocking
- ✅ **Circuit Breaker Enforcement** - Active circuit breaker status checking to prevent cascading failures
- ✅ **Actual Request Routing** - Forward requests to configured backend services with load balancing
- ✅ **Input Validation** - Comprehensive request validation with detailed error messages

## Technology Stack

- **Framework**: Spring Boot 4.0.3
- **Java Version**: 21
- **Database**: H2 (dev), PostgreSQL (prod), MySQL (prod)
- **Cache**: Redis
- **Security**: Spring Security + JWT (JJWT)
- **Testing**: JUnit 5, Mockito
- **Monitoring**: Prometheus + Grafana + Jaeger
- **Logging**: Logback with Logstash encoder (JSON)
- **Distributed Tracing**: Spring Cloud Sleuth + OpenTelemetry
- **Build**: Maven
- **Container**: Docker & Docker Compose

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+
- PostgreSQL 16 (for production)
- Redis 7+ (optional, for caching)

### Development (H2 In-Memory Database)

```bash
# Clone the repository
git clone <repository-url>
cd apigateway

# Build the project
mvn clean package

# Run the application
java -jar target/apigateway-0.0.1-SNAPSHOT.jar

# Application will be available at http://localhost:8080
```

### Production (PostgreSQL)

```bash
# Using Docker Compose (Recommended)
docker-compose up -d

# Or manually with PostgreSQL
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=postgres"
```

## Configuration

### JWT Authentication
Update JWT secret in `application.properties`:
```properties
app.jwt.secret=your-secret-key-minimum-32-characters-required
app.jwt.expiration=86400000  # 24 hours in milliseconds
```

### Database Profiles
- **Development**: `application.properties` (H2)
- **Production**: `application-postgres.properties` (PostgreSQL)

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=RequestLogServiceTest
```

### Integration Tests
```bash
# Run test script
./test-api-simple.sh
```

## Docker Deployment

### Using Docker Compose (Complete Stack)
```bash
# Start all services (PostgreSQL, Redis, API Gateway)
docker-compose up -d

# View logs
docker-compose logs -f apigateway

# Stop all services
docker-compose down
```

## Security

### JWT Secret Management
⚠️ **IMPORTANT**: Change the default JWT secret key in production:
```properties
app.jwt.secret=<your-secure-random-32-character-key>
```

### Database Credentials
Update PostgreSQL credentials in production

## API Endpoints

### Authentication
```bash
# Generate token
curl -X POST http://localhost:8080/api/auth/token \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"pass123"}'

# Use token in requests
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/logs/requests
```

### Request & Audit Logging
- `POST /api/logs/requests` - Create request log
- `GET /api/logs/requests` - List request logs
- `GET /api/logs/audit` - List audit logs

### Circuit Breaker
- `GET /circuit-breaker/states` - Get all circuit breaker states
- `GET /circuit-breaker/state/{serviceName}` - Get state for service
- `GET /circuit-breaker/allow/{serviceName}` - Check if request allowed

### Service Instances
- `GET /service-instances` - Get all instances
- `GET /service-instances/service/{serviceName}` - Get by service name

## Monitoring & Observability

### Health Checks
```bash
# Liveness probe
curl http://localhost:8080/actuator/health/liveness

# Readiness probe
curl http://localhost:8080/actuator/health/readiness

# Full health details
curl http://localhost:8080/actuator/health
```

### Metrics & Prometheus
```bash
# View all metrics in Prometheus format
curl http://localhost:8080/actuator/prometheus

# Metrics include:
# - JVM metrics (memory, GC, threads)
# - Process metrics (CPU, uptime)
# - HTTP request metrics
# - Custom application metrics
```

### Distributed Tracing (Jaeger)
```bash
# Access Jaeger UI at http://localhost:16686
# View traces, spans, and latency data
# Track requests across microservices
```

### Grafana Dashboards
```bash
# Access Grafana at http://localhost:3000
# Default credentials: admin/admin
# Query Prometheus data with Grafana
```

## Logging

### Structured Logging
Logs are output in JSON format for easy parsing by centralized logging systems:

```json
{
  "timestamp": "2026-03-31T22:00:00.000Z",
  "level": "INFO",
  "logger": "com.apigateway.service.RequestLogService",
  "message": "Request logged successfully",
  "traceId": "abc123...",
  "spanId": "xyz789...",
  "app": "apigateway"
}
```

### Log Levels per Module
Configure in `logback-spring.xml`:
- `com.apigateway` - INFO (general logging)
- `com.apigateway.security` - DEBUG (authentication details)
- `com.apigateway.service` - INFO (business logic)
- `org.springframework.web` - INFO (HTTP requests)

### Log Files
- Location: `logs/apigateway.log`
- Rolling: Daily + 10MB size limit
- Retention: 30 days

## Error Handling

All errors are returned in consistent JSON format:

```json
{
  "status": 400,
  "message": "Invalid request parameters",
  "error": "Validation Error",
  "path": "/api/logs/requests",
  "timestamp": "2026-03-31T22:00:00",
  "traceId": "abc123..."
}
```

### HTTP Status Codes
- `200` - Success
- `400` - Bad Request (validation errors)
- `401` - Unauthorized (missing/invalid token)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found
- `429` - Too Many Requests (rate limit exceeded)
- `503` - Service Unavailable (circuit breaker OPEN)
- `500` - Internal Server Error

## Advanced Features

### Rate Limiting Enforcement

The API Gateway actively enforces rate limiting policies at request time.

**How it works:**
1. RateLimitingFilter intercepts each request
2. Checks for applicable rate limit policy based on route
3. Selects rate limiter algorithm (FIXED_WINDOW, SLIDING_WINDOW, TOKEN_BUCKET)
4. Returns 429 Too Many Requests if limit is exceeded

**Configuration:**
```bash
# Create a rate limit policy (max 10 requests per 60 seconds, per user)
curl -X POST http://localhost:8080/api/rate-limit-policies \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": "550e8400-e29b-41d4-a716-446655440000",
    "limitCount": 10,
    "windowDurationSeconds": 60,
    "perType": "USER",
    "algorithmType": "FIXED_WINDOW"
  }'
```

**Rate Limit Response Headers:**
```
X-RateLimit-Limit: 10
X-RateLimit-Remaining: 3
X-RateLimit-Reset: 1648730400
```

### Circuit Breaker Enforcement

The API Gateway monitors backend service health and prevents cascading failures.

**Circuit Breaker States:**
- **CLOSED**: Normal operation, requests are forwarded
- **OPEN**: Service is down, requests are blocked (503 status)
- **HALF_OPEN**: Service recovering, test requests allowed

**Failure Conditions:**
- Failure count exceeds threshold (5 failures)
- Failure rate exceeds threshold (50%)
- Service responds with error status

**Recovery:**
- Automatically transitions to HALF_OPEN after 60 seconds
- Successful request in HALF_OPEN closes the circuit
- Returns to CLOSED with reset metrics

**Monitor Circuit Breaker:**
```bash
# Get circuit breaker state for a service
curl -H "Authorization: Bearer <token>" \
  http://localhost:8080/circuit-breaker/state/userService

# Record success/failure manually
curl -X POST -H "Authorization: Bearer <token>" \
  http://localhost:8080/circuit-breaker/failure/userService

# Reset circuit breaker
curl -X POST -H "Authorization: Bearer <token>" \
  http://localhost:8080/circuit-breaker/reset/userService
```

### Actual Request Routing

The API Gateway forwards requests to configured backend services based on predefined routes.

**How it works:**
1. RequestRoutingFilter receives incoming API request
2. Matches request path to a configured Route
3. Selects an active RouteTarget (backend service)
4. Forwards HTTP request with proper headers
5. Records success/failure in circuit breaker
6. Returns backend response to client

**Configure Routes:**
```bash
# Create a route
curl -X POST http://localhost:8080/api/routes \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "path": "/api/users",
    "serviceName": "userService",
    "timeoutMs": 5000,
    "retryCount": 2,
    "authRequired": true,
    "isActive": true
  }'

# Add backend targets for the route
curl -X POST http://localhost:8080/api/route-targets \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": "550e8400-e29b-41d4-a716-446655440000",
    "instanceUrl": "http://user-service:8081",
    "weight": 1,
    "version": "v1",
    "isActive": true
  }'
```

**Request Flow Example:**
```
Client Request → API Gateway
  ↓
  1. RateLimitingFilter - Check rate limits
  2. CircuitBreakerFilter - Check circuit breaker status  
  3. RequestRoutingFilter - Forward to backend
  ↓
Backend Service (e.g., http://user-service:8081)
  ↓
  Returns response with status code
  ↓
API Gateway
  ↓
  1. Record success/failure
  2. Add response headers
  3. Return response to client
  ↓
Client Response
```

### Input Validation

All API endpoints validate request data using Spring's @Valid annotation and validation constraints.

**Validation Features:**
- Automatic request body validation
- Detailed error messages for invalid fields
- 400 Bad Request responses with validation errors
- Custom validation constraints

**Validation Example:**
```bash
# Invalid request (missing required field)
curl -X POST http://localhost:8080/api/rate-limit-policies \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "limitCount": 10
    # Missing: routeId, windowDurationSeconds, perType, algorithmType
  }'

# Response (400 Bad Request):
{
  "status": 400,
  "message": "Validation failed",
  "error": "Invalid request",
  "fieldErrors": {
    "routeId": "Route ID is required",
    "windowDurationSeconds": "Window duration must be at least 1 second",
    "perType": "Per type is required",
    "algorithmType": "Algorithm type is required"
  }
}
```

**Validated DTOs:**
- `RateLimitPolicyDto` - Route ID, limit count, window duration, per type, algorithm type
- `RouteDto` - Path, service name, timeout, retry count
- `RouteTargetDto` - Route ID, instance URL, weight
- `RequestLogDto` - Request ID, method, path, status code, response time
- `ServiceInstanceDto` - Service name, host, port, health status, weight
- `AuditLogDto` - Action, entity type/ID, changed by user
