# API Gateway - Complete Documentation

A production-ready Spring Boot API Gateway with request routing, rate limiting, circuit breaker patterns, distributed tracing, comprehensive logging, and JWT authentication.

---

## Table of Contents

1. [Overview](#overview)
2. [How It Actually Works](#how-it-actually-works)
3. [Quick Start](#quick-start)
4. [What Each Component Does](#what-each-component-does)
5. [Complete Setup Example](#complete-setup-example)
6. [Rate Limiting Algorithms](#rate-limiting-algorithms)
7. [Circuit Breaker Pattern](#circuit-breaker-pattern)
8. [API Endpoints](#api-endpoints)
9. [Database Schema](#database-schema)
10. [Troubleshooting](#troubleshooting)

---

## Overview

### What is an API Gateway?

An API Gateway is a **reverse proxy** that sits between clients and backend services:

```
┌─────────────┐
│   Clients   │ (Browser, Mobile, API Consumers)
└──────┬──────┘
       │ HTTP Request
       ▼
┌───────────────────────────────┐
│   API GATEWAY (This App)      │
│   Port: 8080                  │
│                               │
│  ├─ Authentication (JWT)      │
│  ├─ Rate Limiting             │
│  ├─ Circuit Breaker           │
│  ├─ Request Routing           │
│  └─ Logging                   │
└──────┬──────────────────────┬─┘
       │ HTTP Forward         │
       ▼                      ▼
┌──────────────┐        ┌──────────────┐
│  Service-1   │        │  Service-2   │
│  Port: 8081  │        │  Port: 8082  │
└──────────────┘        └──────────────┘
```

### Why Use an API Gateway?

**Instead of this (duplicated logic everywhere):**
```
Frontend → Service A (auth, limits, logging)
         → Service B (auth, limits, logging)
         → Service C (auth, limits, logging)
```

**Do this (centralized security & management):**
```
Frontend → API Gateway (auth, limits, logging)
             ├─ Service A
             ├─ Service B
             └─ Service C
```

### Key Capabilities

| Feature | Purpose |
|---------|---------|
| **JWT Authentication** | Validate tokens, extract user info |
| **Rate Limiting** | Prevent abuse (100 req/min per user) |
| **Circuit Breaker** | Stop calling failing services (auto-recovery) |
| **Request Routing** | Direct /api/users → user-service, /api/orders → order-service |
| **Load Balancing** | Distribute traffic (80% to server A, 20% to server B) |
| **Request Logging** | Audit trail of all API calls |
| **Audit Logging** | Track who changed what, when |
| **Metrics** | Prometheus metrics for monitoring |

---

## How It Actually Works

### Real Request Flow (Step-by-Step)

**Client Request:**
```bash
curl http://localhost:8080/api/orders/123 \
  -H "Authorization: Bearer eyJ..."
```

**Inside the Gateway (What Happens):**

```
1️⃣ JwtAuthenticationFilter
   ├─ Extracts JWT from Authorization header
   ├─ Validates signature (is token real?)
   ├─ Checks expiration (is token fresh?)
   ├─ If invalid → Return 401 UNAUTHORIZED
   └─ If valid → Extract user info, continue

2️⃣ RateLimitingFilter
   ├─ Query: "What's the rate limit for /api/orders?"
   ├─ Query: "Has this user made too many requests?"
   ├─ Count from database: 95 requests this minute
   ├─ Limit: 100 per minute
   ├─ Decision: ✓ Allow (95 < 100)
   └─ If exceeded → Return 429 TOO_MANY_REQUESTS

3️⃣ CircuitBreakerFilter
   ├─ Query: "Which backend handles /api/orders?"
   ├─ Answer: "order-service"
   ├─ Query: "Is order-service healthy?"
   ├─ Check: Last 5 requests succeeded ✓
   ├─ Circuit State: CLOSED (normal operation)
   └─ If OPEN → Return 503 SERVICE_UNAVAILABLE

4️⃣ RequestRoutingFilter ⭐ THE MAGIC HAPPENS HERE
   ├─ Query: "Get all backends for order-service route"
   ├─ Found: [http://orders:8081 (weight=50%), http://orders:8082 (weight=50%)]
   ├─ Load balance: Randomly pick → http://orders:8081
   ├─ Forward: GET http://orders:8081/api/orders/123
   │           With all original headers (Authorization, etc)
   ├─ Wait for response
   ├─ Record: success/failure in circuit breaker
   └─ Return response to client

5️⃣ Logging
   └─ Insert into request_log table:
      ├─ Endpoint: /api/orders/123
      ├─ Status: 200
      ├─ Response time: 245ms
      ├─ User: user123
      └─ IP: 192.168.1.100
```

**Key Insight:**

⚠️ **The gateway doesn't have `/api/orders` endpoint itself!**

Instead:
- It listens on port 8080 for **ANY** path
- It looks up the path in the **database** table `routes`
- It forwards to the configured backend
- **Admin can add/remove routes without recompiling code!**

### Without a Route Configured

```bash
GET http://localhost:8080/api/orders/123
→ 404 NOT FOUND (no route in database)
```

### After Admin Creates Route

```bash
# Admin creates route
POST /api/routes
{
  "path": "/api/orders",
  "serviceName": "order-service",
  "isActive": true
}

# Now it works!
GET http://localhost:8080/api/orders/123
→ 200 OK (forwarded to backend)
```

---

## Quick Start

### Prerequisites

```bash
# Check Java version (need 21+)
java -version

# Maven installed
mvn -version
```

### Run Application

```bash
cd /home/tahri/projects/apigateway

# Build
./mvnw clean package -DskipTests

# Run (uses H2 in-memory database for dev)
java -jar target/apigateway-0.0.1-SNAPSHOT.jar

# Or start directly with Maven
./mvnw spring-boot:run
```

### Verify Running

```bash
# Health check
curl http://localhost:8080/actuator/health

# H2 Console (dev only)
# Visit: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
```

---

## What Each Component Does

### Filter Chain (In Order)

The gateway processes **every** request through these 4 filters:

#### 1. JWT Authentication Filter

**What:** Validates JWT tokens and extracts user information

**Example:**
```
Request:  Authorization: Bearer eyJ...
          ↓
Filter:   Validate signature ✓
          Extract claims: user_id=123, roles=[ADMIN]
          ↓
Result:   Continue (or 401 UNAUTHORIZED if invalid)
```

**Status Codes:**
- `200` - Valid token, continue
- `401` - Missing/invalid token, blocked

#### 2. Rate Limiting Filter

**What:** Enforces request quotas per user/IP/global

**Example (100 requests per minute per user):**
```
Request:  From user123
          ↓
Filter:   Count requests from user123 this minute: 95
          Limit: 100
          ↓
Result:   Allow (95 < 100)

Later:
Request:  101st request from user123 this minute
          ↓
Filter:   Count: 100 (at limit)
          ↓
Result:   Block, return 429 TOO_MANY_REQUESTS
```

**Status Codes:**
- `200` - Within limit, continue
- `429` - Rate limit exceeded, blocked

#### 3. Circuit Breaker Filter

**What:** Protects backends from cascading failures

**Example (backend service crashes):**
```
Request 1-5: All fail (connection refused)
          ↓
Filter:   Failure count = 5 → OPEN circuit
          ↓
Request 6: Returns 503 immediately (no backend call!)
Request 7: Still 503 (fast-fail)
...
After 60s: Try recovery (HALF_OPEN state)
Request N: Succeeds → CLOSED circuit (healthy again!)
```

**Status Codes:**
- `200` - Circuit CLOSED, forward request
- `503` - Circuit OPEN, blocked to protect backend

#### 4. Request Routing Filter

**What:** Forwards request to appropriate backend using load balancing

**Example:**
```
Request:  GET /api/orders/123
          ↓
Filter:   Query database: Find route for /api/orders
          Found: route points to "order-service"
          ↓
Filter:   Query database: Find backends for order-service
          Found: [http://orders:8081 (50%), http://orders:8082 (50%)]
          ↓
Filter:   Load balance: Pick one → http://orders:8081
          ↓
Filter:   Forward: GET http://orders:8081/api/orders/123
          ↓
Result:   Return backend response to client
```

**Status Codes:**
- `200-599` - Backend response (passed through)

### Service Layer

Each feature has a dedicated service:

| Service | Responsibility |
|---------|-----------------|
| **RouteService** | Manage routes in database |
| **RouteTargetService** | Manage backends (targets) for routes |
| **RateLimitPolicyService** | Manage rate limit policies |
| **CircuitBreakerService** | Manage circuit breaker state |
| **RateLimitStateService** | Track current rate limit counts |
| **RequestLogService** | Log all requests |
| **AuditLogService** | Log configuration changes |

### Controllers (REST API)

Admins use these endpoints to manage the gateway:

```
POST /api/routes                          ← Create route
POST /api/route-targets                   ← Create backend target
POST /api/rate-limit-policies             ← Create rate limit policy
GET  /api/routes                          ← List routes
GET  /api/route-targets/by-route/{id}     ← List backends for a route
PATCH /api/route-targets/{id}/weight      ← Change load balancing
DELETE /api/routes/{id}                   ← Remove route
```

---

## Complete Setup Example

### Step 1: Admin Authenticates

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin_password"}'

# Response:
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "roles": ["ROLE_ADMIN"]
}

# Save for next steps:
TOKEN="eyJhbGc..."
```

### Step 2: Create Routes

```bash
# Route for /api/orders → order-service
curl -X POST http://localhost:8080/api/routes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "path": "/api/orders",
    "serviceName": "order-service",
    "timeoutMs": 5000,
    "retryCount": 3,
    "authRequired": true,
    "authProvider": "JWT",
    "isActive": true
  }'

# Response:
{
  "id": "route-uuid-1",
  "path": "/api/orders",
  ...
}

# Save for next steps:
ROUTE_ID="route-uuid-1"
```

### Step 3: Create Backend Targets

```bash
# Backend 1 (50% of traffic)
curl -X POST http://localhost:8080/api/route-targets \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": "'$ROUTE_ID'",
    "instanceUrl": "http://order-service:8081",
    "weight": 50,
    "version": "1.0.0",
    "isActive": true
  }'

# Backend 2 (50% of traffic)
curl -X POST http://localhost:8080/api/route-targets \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": "'$ROUTE_ID'",
    "instanceUrl": "http://order-service:8082",
    "weight": 50,
    "version": "1.0.0",
    "isActive": true
  }'
```

### Step 4: Create Rate Limit Policy

```bash
curl -X POST http://localhost:8080/api/rate-limit-policies \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "path": "/api/orders",
    "requestsPerMinute": 100,
    "algorithm": "TOKEN_BUCKET",
    "isActive": true
  }'
```

### Step 5: User Makes Request

```bash
# Get user token
USER_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user123","password":"password"}' \
  | jq -r '.token')

# Make request to gateway (NOT directly to backend!)
curl -X GET http://localhost:8080/api/orders/123 \
  -H "Authorization: Bearer $USER_TOKEN"

# Gateway handles all the work:
# 1. Validates JWT ✓
# 2. Checks rate limit ✓
# 3. Checks circuit breaker ✓
# 4. Load balances to backend ✓
# 5. Logs request ✓

# Response: 200 OK with order data
```

---

## Rate Limiting Algorithms

### Fixed Window Algorithm

**Concept:** Divide time into 60-second chunks, count requests per chunk

```
Minute 1 (0-60s):   0 - 60 requests allowed
Minute 2 (60-120s): 60 - 120 requests allowed
Minute 3 (120-180s): 120 - 180 requests allowed
```

**Problem:** Burst attacks at boundaries
```
User makes 100 requests at 59th second
User makes 100 requests at 60th second  ← New bucket!
= 200 requests in 1 second (spike!)
```

**Best for:** Simple APIs, forgiving clients

### Sliding Window Algorithm

**Concept:** Use exact timestamps, slide window through time

```
Keep timestamps of last 100 requests:
[1000ms, 2000ms, 3000ms, ..., 99000ms, 100000ms]

New request at 101000ms:
- Remove timestamps older than (101000 - 60000) = 41000
- Remaining: [41500ms, 42000ms, ..., 100000ms] (89 requests)
- Add new one: 89 < 100, ALLOW
```

**Pros:** Perfect accuracy, prevents burst attacks
**Cons:** High memory (stores all timestamps)

**Best for:** Critical APIs (payments, high-value operations)

### Token Bucket Algorithm (Recommended)

**Concept:** Bucket fills at constant rate, each request uses 1 token

```
Capacity: 100 tokens
Refill rate: 100 tokens per 60 seconds = 1.67 tokens/second

Timeline:
0s:    Bucket = 100
5s:    Bucket = 100 + (5 × 1.67) = 108.35 → capped at 100
10s:   Bucket = 100 (still full)
10.1s: Request → Bucket = 99 (still has room for bursts!)
10.2s: Request → Bucket = 98
...
61s:   Refill continues → Bucket = 100 (refresh at constant rate)
```

**Pros:** Allows controlled bursts, handles traffic spikes gracefully
**Best for:** Most production APIs

---

## Circuit Breaker Pattern

### How It Works

The circuit breaker monitors backend health and prevents cascading failures:

```
┌─────────────────────────────────────────────────────────────┐
│                    CLOSED (Normal)                           │
│  - All requests pass through to backend                      │
│  - Monitor for failures                                      │
│  [Trigger: 5 failures OR 50% failure rate]                 │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                    OPEN (Problem!)                           │
│  - ALL requests rejected immediately (503)                  │
│  - NO calls to backend                                      │
│  - Fast-fail protects backend from overload                │
│  - Wait 60 seconds for recovery                            │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                  HALF_OPEN (Testing)                         │
│  - Allow a few test requests                                │
│  - If success → Go to CLOSED (recovered!)                  │
│  - If failure → Go back to OPEN (still down)               │
└────────────────────┬────────────────────────────────────────┘
                     │
                     └──────→ CLOSED
```

### Example Scenario

```
15:00:00 - Request 1 → Backend error, failure count = 1
15:00:01 - Request 2 → Backend error, failure count = 2
15:00:02 - Request 3 → Backend error, failure count = 3
15:00:03 - Request 4 → Backend error, failure count = 4
15:00:04 - Request 5 → Backend error, failure count = 5
          ↓
          Circuit OPENS (threshold reached)
          ↓
15:00:05 - Request 6 → 503 SERVICE UNAVAILABLE (no backend call!)
15:00:06 - Request 7 → 503 SERVICE UNAVAILABLE (no backend call!)
...
15:01:04 - 60 seconds elapsed
          ↓
          Circuit goes HALF_OPEN (test recovery)
          ↓
15:01:05 - Request N → Forward to backend (test request)
          ↓
          Backend responds 200 OK ✓
          ↓
          Circuit CLOSES (healthy again!)
          ↓
15:01:06 - Request N+1 → Forward normally
```

---

## API Endpoints

### Admin Endpoints (Require ROLE_ADMIN)

#### Routes

```bash
# Create
POST /api/routes
{
  "path": "/api/orders",
  "serviceName": "order-service",
  "timeoutMs": 5000,
  "retryCount": 3,
  "authRequired": true,
  "authProvider": "JWT",
  "isActive": true
}

# Read
GET /api/routes                              # List all (paginated)
GET /api/routes/{id}                         # Get one
GET /api/routes/active/list                  # Active only
GET /api/routes/search?path=/api/orders      # Search by path
GET /api/routes/by-service?serviceName=...   # By service

# Update
PUT /api/routes/{id}
PATCH /api/routes/{id}/activate
PATCH /api/routes/{id}/deactivate

# Delete
DELETE /api/routes/{id}  # Also deletes associated targets
```

#### Route Targets (Backends)

```bash
# Create
POST /api/route-targets
{
  "routeId": "...",
  "instanceUrl": "http://order-service:8081",
  "weight": 50,
  "version": "1.0.0",
  "isActive": true
}

# Read
GET /api/route-targets                           # List all
GET /api/route-targets/{id}                      # Get one
GET /api/route-targets/by-route/{routeId}        # Targets for route
GET /api/route-targets/active/by-route/{routeId} # Active only
GET /api/route-targets/search?url=...            # By URL

# Update
PUT /api/route-targets/{id}
PATCH /api/route-targets/{id}/weight?weight=80   # Change load balance
PATCH /api/route-targets/{id}/activate
PATCH /api/route-targets/{id}/deactivate

# Delete
DELETE /api/route-targets/{id}
DELETE /api/route-targets/by-route/{routeId}     # Delete all for route
```

#### Rate Limit Policies

```bash
POST /api/rate-limit-policies
GET  /api/rate-limit-policies
GET  /api/rate-limit-policies/by-path?path=/api/orders
```

#### Circuit Breaker Management

```bash
GET  /api/circuit-breaker              # All states
GET  /api/circuit-breaker/{serviceName} # Specific service
POST /api/circuit-breaker/{serviceName}/reset
```

#### Monitoring

```bash
GET /api/request-logs                  # All requests (paginated)
GET /api/request-logs/{id}             # Specific request
GET /api/audit-logs                    # Configuration changes
GET /actuator/health                   # Health check
GET /actuator/prometheus               # Metrics for Prometheus
```

### User Endpoints

All routes created by admins are accessible to users:

```bash
GET  /api/orders/123          # Routes to order-service:8081
POST /api/orders              # Routes to order-service:8081
PUT  /api/users/456           # Routes to user-service:8082
DELETE /api/products/789      # Routes to product-service:8083
```

---

## Database Schema

```sql
-- Define where requests should be routed
CREATE TABLE routes (
  id UUID PRIMARY KEY,
  path VARCHAR(255) NOT NULL,        -- /api/orders
  service_name VARCHAR(100),         -- order-service
  timeout_ms INT,                    -- 5000
  retry_count INT,                   -- 3
  auth_required BOOLEAN,
  auth_provider VARCHAR(50),         -- JWT
  is_active BOOLEAN,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

-- Define the actual backend servers
CREATE TABLE route_targets (
  id UUID PRIMARY KEY,
  route_id UUID NOT NULL,
  instance_url VARCHAR(255),         -- http://orders:8081
  weight INT,                        -- 50 (load balance weight)
  version VARCHAR(50),
  is_active BOOLEAN,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (route_id) REFERENCES routes(id)
);

-- Track rate limit policies
CREATE TABLE rate_limit_policies (
  id UUID PRIMARY KEY,
  path VARCHAR(255),                 -- /api/orders
  requests_per_minute INT,           -- 100
  algorithm VARCHAR(50),             -- TOKEN_BUCKET
  is_active BOOLEAN,
  created_at TIMESTAMP
);

-- Track circuit breaker state
CREATE TABLE circuit_breaker_state (
  id UUID PRIMARY KEY,
  service_name VARCHAR(100) UNIQUE,  -- order-service
  state VARCHAR(20),                 -- CLOSED, OPEN, HALF_OPEN
  failure_count INT,
  last_failure_time TIMESTAMP,
  open_at TIMESTAMP
);

-- Audit trail of all requests
CREATE TABLE request_logs (
  id UUID PRIMARY KEY,
  endpoint VARCHAR(255),             -- /api/orders/123
  method VARCHAR(10),                -- GET
  status_code INT,                   -- 200
  user_id VARCHAR(255),
  ip_address VARCHAR(45),
  response_time_ms INT,              -- 245
  route_id UUID,
  created_at TIMESTAMP
);

-- Track configuration changes
CREATE TABLE audit_logs (
  id UUID PRIMARY KEY,
  action VARCHAR(50),                -- CREATE, UPDATE, DELETE
  entity VARCHAR(100),               -- Route, RouteTarget
  entity_id UUID,
  username VARCHAR(100),
  previous_value JSONB,
  new_value JSONB,
  created_at TIMESTAMP
);
```

---

## Troubleshooting

### Problem: "404 Not Found"

**Cause:** No route configured for that path

**Solution:**
```bash
curl -X POST /api/routes -d '{"path":"/api/orders","serviceName":"order-service"}'
```

### Problem: "429 Too Many Requests"

**Cause:** Rate limit exceeded

**Solution:**
```bash
# Increase rate limit
curl -X PUT /api/rate-limit-policies/{id} -d '{"requestsPerMinute":200}'
```

### Problem: "503 Service Unavailable"

**Cause:** Circuit breaker is OPEN (backend is down)

**Solution:** Wait 60 seconds or manually close:
```bash
curl -X POST /api/circuit-breaker/{serviceName}/close
```

### Problem: Slow Responses

**Check:**
```bash
# 1. Review request logs
curl /api/request-logs

# 2. Check if timeout is too short
curl /api/routes/{id}

# 3. Increase timeout if needed
curl -X PUT /api/routes/{id} -d '{"timeoutMs":10000}'
```

---

## Project Structure

```
src/main/java/com/apigateway/
├── controller/         # REST endpoints
├── service/           # Business logic
├── filter/            # Request filters
├── model/             # Database entities
├── repository/        # Spring Data interfaces
├── dto/               # Data transfer objects
├── ratelimit/         # Algorithms (Fixed, Sliding, Token)
└── ApigatewayApplication.java
```

---

## Further Reading

- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Spring Security**: https://spring.io/projects/spring-security
- **Resilience4j Circuit Breaker**: https://resilience4j.readme.io/docs/circuitbreaker

---

## Summary

This API Gateway provides:

✅ **Single entry point** for all requests (localhost:8080)
✅ **Dynamic routing** (change routes without recompiling code)
✅ **Security** (JWT authentication, role-based access)
✅ **Reliability** (circuit breaker, rate limiting)
✅ **Observability** (request logs, audit logs, metrics)
✅ **Load balancing** (distribute traffic across backends)

Everything is **API-driven** - no code changes needed to add/remove/modify routes, rate limits, or circuit breaker policies!
