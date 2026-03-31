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
- ✅ **Database Profiles** - Support for H2 (dev) and PostgreSQL (prod)
- ✅ **Unit Tests** - Service layer unit tests with Mockito
- ✅ **Integration Tests** - End-to-end API testing
- ✅ **Health Checks** - Built-in health check endpoints

## Technology Stack

- **Framework**: Spring Boot 4.0.3
- **Java Version**: 21
- **Database**: H2 (dev), PostgreSQL (prod)
- **Cache**: Redis
- **Security**: Spring Security + JWT (JJWT)
- **Testing**: JUnit 5, Mockito
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
