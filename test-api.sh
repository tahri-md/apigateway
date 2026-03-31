#!/bin/bash

# API Gateway Comprehensive Test Script
# Tests all RequestLog and AuditLog endpoints

BASE_URL="http://localhost:8080"
LOG_FILE="/tmp/api_test_results_$(date +%Y%m%d_%H%M%S).log"
TIMEOUT=10

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

PASSED=0
FAILED=0

# Initialize log file
echo "=========================================="  | tee "$LOG_FILE"
echo "API Gateway Comprehensive Test Script"       | tee -a "$LOG_FILE"
echo "Started at: $(date '+%Y-%m-%d %H:%M:%S')"   | tee -a "$LOG_FILE"
echo "Base URL: $BASE_URL"                         | tee -a "$LOG_FILE"
echo "Log file: $LOG_FILE"                         | tee -a "$LOG_FILE"
echo "Timeout: ${TIMEOUT}s"                        | tee -a "$LOG_FILE"
echo "=========================================="  | tee -a "$LOG_FILE"
echo ""                                             | tee -a "$LOG_FILE"

# Wait for server to be ready
echo -e "${YELLOW}Waiting for server to be ready...${NC}" | tee -a "$LOG_FILE"
max_attempts=30
attempt=0
while [ $attempt -lt $max_attempts ]; do
  if curl -s --max-time 2 "${BASE_URL}/api/logs/requests" > /dev/null 2>&1; then
    echo -e "${GREEN}Server is ready!${NC}" | tee -a "$LOG_FILE"
    break
  fi
  ((attempt++))
  sleep 1
done

if [ $attempt -eq $max_attempts ]; then
  echo -e "${RED}Server failed to start after ${max_attempts}s${NC}" | tee -a "$LOG_FILE"
  exit 1
fi
echo "" | tee -a "$LOG_FILE"

# Function to test endpoint
test_endpoint() {
  local test_num=$1
  local method=$2
  local endpoint=$3
  local description=$4
  local data=$5
  
  echo -e "${BLUE}Test $test_num: $method $endpoint - $description${NC}" | tee -a "$LOG_FILE"
  
  local http_code
  local response
  local curl_output
  
  if [ "$method" = "POST" ] || [ "$method" = "PUT" ]; then
    curl_output=$(curl -s --max-time $TIMEOUT -w "\n%{http_code}" -X "$method" "${BASE_URL}${endpoint}" \
      -H "Content-Type: application/json" \
      -d "$data" 2>&1)
  else
    curl_output=$(curl -s --max-time $TIMEOUT -w "\n%{http_code}" -X "$method" "${BASE_URL}${endpoint}" \
      -H "Content-Type: application/json" 2>&1)
  fi
  
  # Extract http code (last line) and response body (everything else)
  http_code=$(echo "$curl_output" | tail -n1)
  body=$(echo "$curl_output" | sed '$d')
  
  echo "Request: $method ${BASE_URL}${endpoint}" | tee -a "$LOG_FILE"
  if [ ! -z "$data" ]; then
    echo "Data: $data" | tee -a "$LOG_FILE"
  fi
  echo "HTTP Status: $http_code" | tee -a "$LOG_FILE"
  echo "Response:" | tee -a "$LOG_FILE"
  echo "$body" | head -c 500 | tee -a "$LOG_FILE"
  echo "" | tee -a "$LOG_FILE"
  
  # Check if successful (2xx status code)
  if [[ $http_code =~ ^2[0-9]{2}$ ]]; then
    echo -e "${GREEN}✓ PASSED${NC}" | tee -a "$LOG_FILE"
    ((PASSED++))
  else
    echo -e "${RED}✗ FAILED - HTTP $http_code${NC}" | tee -a "$LOG_FILE"
    ((FAILED++))
  fi
  echo "" | tee -a "$LOG_FILE"
  
  # Return the body for extraction of ID
  echo "$body"
}

# Test 1: GET all request logs
echo "=== REQUEST LOG ENDPOINTS ===" | tee -a "$LOG_FILE"
REQ_LOG_RESPONSE=$(test_endpoint 1 "GET" "/api/logs/requests" "List all request logs")

# Test 2: POST create request log
REQUEST_ID="test-req-$(date +%s)"
test_endpoint 2 "POST" "/api/logs/requests" "Create new request log" '{
  "requestId": "'$REQUEST_ID'",
  "userId": "testuser1",
  "ipAddress": "192.168.1.100",
  "method": "GET",
  "path": "/api/users",
  "routedToService": "UserService",
  "routedToInstance": "user-instance-1",
  "statusCode": 200,
  "responseTimeMs": 150,
  "rateLimitRemaining": 99,
  "circuitBreakerState": "CLOSED"
}' > /tmp/req_log_response.txt

# Extract request log ID
REQUEST_LOG_ID=$(cat /tmp/req_log_response.txt | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)

# Test 3: GET specific request log by ID
if [ ! -z "$REQUEST_LOG_ID" ]; then
  test_endpoint 3 "GET" "/api/logs/requests/${REQUEST_LOG_ID}" "Get specific request log by ID"
else
  echo -e "${YELLOW}Skipping Test 3: Could not extract ID from previous response${NC}" | tee -a "$LOG_FILE"
fi

# Test 4: GET request log by request ID
test_endpoint 4 "GET" "/api/logs/requests/request/${REQUEST_ID}" "Get request log by request ID"

# Test 5: PUT update request log
if [ ! -z "$REQUEST_LOG_ID" ]; then
  test_endpoint 5 "PUT" "/api/logs/requests/${REQUEST_LOG_ID}" "Update request log" '{
    "userId": "updateduser",
    "statusCode": 201,
    "responseTimeMs": 200
  }'
fi

echo "=== AUDIT LOG ENDPOINTS ===" | tee -a "$LOG_FILE"

# Test 6: GET all audit logs
test_endpoint 6 "GET" "/api/logs/audit" "List all audit logs"

# Test 7: POST create audit log
ENTITY_ID="entity-$(date +%s)"
test_endpoint 7 "POST" "/api/logs/audit" "Create new audit log" '{
  "action": "CREATE",
  "entityType": "Route",
  "entityId": "'$ENTITY_ID'",
  "changedBy": "admin"
}' > /tmp/audit_log_response.txt

# Extract audit log ID
AUDIT_LOG_ID=$(cat /tmp/audit_log_response.txt | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)

# Test 8: GET specific audit log by ID
if [ ! -z "$AUDIT_LOG_ID" ]; then
  test_endpoint 8 "GET" "/api/logs/audit/${AUDIT_LOG_ID}" "Get specific audit log by ID"
else
  echo -e "${YELLOW}Skipping Test 8: Could not extract ID from previous response${NC}" | tee -a "$LOG_FILE"
fi

# Test 9: GET audit logs by entity
test_endpoint 9 "GET" "/api/logs/audit/entity/Route/${ENTITY_ID}" "Get audit logs by entity"

# Test 10: POST another audit log
test_endpoint 10 "POST" "/api/logs/audit" "Create second audit log" '{
  "action": "UPDATE",
  "entityType": "RateLimitPolicy",
  "entityId": "policy-001",
  "changedBy": "admin",
  "oldValue": "{\"limit\": 100}",
  "newValue": "{\"limit\": 150}"
}'

echo "=== CIRCUIT BREAKER ENDPOINTS ===" | tee -a "$LOG_FILE"

# Test 11: GET circuit breaker state
test_endpoint 11 "GET" "/circuit-breaker/state/UserService" "Get circuit breaker state"

# Test 12: GET all circuit breaker states
test_endpoint 12 "GET" "/circuit-breaker/states" "Get all circuit breaker states"

# Test 13: Check if request allowed
test_endpoint 13 "GET" "/circuit-breaker/allow/UserService" "Check if request allowed"

echo "=== RATE LIMIT POLICY ENDPOINTS ===" | tee -a "$LOG_FILE"

# Test 14: GET all rate limit policies
test_endpoint 14 "GET" "/api/rate-limit-policies" "Get all rate limit policies"

# Test 15: POST create rate limit policy
test_endpoint 15 "POST" "/api/rate-limit-policies" "Create rate limit policy" '{
  "name": "test-policy-'$(date +%s)'",
  "serviceName": "TestService",
  "algorithmType": "FIXED_WINDOW",
  "requestLimit": 100,
  "windowDurationSeconds": 60
}'

echo "=== SERVICE INSTANCES ENDPOINTS ===" | tee -a "$LOG_FILE"

# Test 16: GET all service instances
test_endpoint 16 "GET" "/service-instances" "Get all service instances"

# Test 17: GET service instances by service name
test_endpoint 17 "GET" "/service-instances/service/UserService" "Get service instances by name"

echo "" | tee -a "$LOG_FILE"
echo "=========================================="          | tee -a "$LOG_FILE"
echo "Test Results Summary"                               | tee -a "$LOG_FILE"
echo "=========================================="          | tee -a "$LOG_FILE"
echo -e "${GREEN}Passed: $PASSED${NC}"                     | tee -a "$LOG_FILE"
echo -e "${RED}Failed: $FAILED${NC}"                       | tee -a "$LOG_FILE"
TOTAL=$((PASSED + FAILED))
echo "Total: $TOTAL"                                       | tee -a "$LOG_FILE"
PERCENTAGE=$((PASSED * 100 / TOTAL))
echo "Success Rate: ${PERCENTAGE}%"                        | tee -a "$LOG_FILE"
echo "=========================================="          | tee -a "$LOG_FILE"
echo "Completed at: $(date '+%Y-%m-%d %H:%M:%S')"        | tee -a "$LOG_FILE"
echo ""                                                    | tee -a "$LOG_FILE"

if [ $FAILED -eq 0 ]; then
  echo -e "${GREEN}✓ All tests passed!${NC}"               | tee -a "$LOG_FILE"
else
  echo -e "${RED}✗ Some tests failed. Check $LOG_FILE for details${NC}" | tee -a "$LOG_FILE"
fi

echo ""
echo "Full log saved to: $LOG_FILE"
