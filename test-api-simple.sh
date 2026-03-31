#!/bin/bash

# Simple API Gateway Test Script
# Tests core RequestLog and AuditLog endpoints

BASE_URL="http://localhost:8080"
LOG_FILE="/tmp/api_test_$(date +%Y%m%d_%H%M%S).log"
PASSED=0
FAILED=0

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo "=========================================="  | tee "$LOG_FILE"
echo "API Gateway Test Suite"                     | tee -a "$LOG_FILE"
echo "Started: $(date)"                           | tee -a "$LOG_FILE"
echo "=========================================="  | tee -a "$LOG_FILE"
echo ""                                             | tee -a "$LOG_FILE"

# Wait for server
echo -e "${YELLOW}Waiting for server...${NC}" | tee -a "$LOG_FILE"
for i in {1..30}; do
  if curl -s --max-time 2 "${BASE_URL}/api/logs/requests" > /dev/null 2>&1; then
    echo -e "${GREEN}Server ready!${NC}" | tee -a "$LOG_FILE"
    break
  fi
  sleep 1
done
echo "" | tee -a "$LOG_FILE"

test_endpoint() {
  local num=$1
  local method=$2
  local path=$3
  local desc=$4
  local data=$5
  
  echo -e "${BLUE}Test $num: $method $path${NC} - $desc" | tee -a "$LOG_FILE"
  
  local result
  if [ -z "$data" ]; then
    result=$(curl -s -w "\n%{http_code}" -X "$method" "${BASE_URL}${path}" \
      -H "Content-Type: application/json" 2>&1)
  else
    result=$(curl -s -w "\n%{http_code}" -X "$method" "${BASE_URL}${path}" \
      -H "Content-Type: application/json" -d "$data" 2>&1)
  fi
  
  local http_code=$(echo "$result" | tail -n1)
  local body=$(echo "$result" | sed '$d')
  
  echo "  Status: $http_code" | tee -a "$LOG_FILE"
  
  if [[ $http_code =~ ^2[0-9]{2}$ ]]; then
    echo -e "  ${GREEN}✓ PASSED${NC}" | tee -a "$LOG_FILE"
    ((PASSED++))
  else
    echo -e "  ${RED}✗ FAILED${NC}" | tee -a "$LOG_FILE"
    ((FAILED++))
    echo "  Response: $(echo "$body" | head -c 200)" | tee -a "$LOG_FILE"
  fi
  echo "" | tee -a "$LOG_FILE"
  
  echo "$body"
}

# REQUEST LOG TESTS
echo -e "${YELLOW}=== REQUEST LOG TESTS ===${NC}" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

test_endpoint 1 "POST" "/api/logs/requests" "Create RequestLog" \
  '{"requestId":"req-'$(date +%s)'","userId":"testuser","ipAddress":"192.168.1.1","method":"GET","path":"/api/test","statusCode":200,"responseTimeMs":150,"createdAt":"'$(date -u +%Y-%m-%dT%H:%M:%S.000Z)'"}'

test_endpoint 2 "GET" "/api/logs/requests" "Get all RequestLogs"

test_endpoint 3 "GET" "/api/logs/requests/request/req-001" "Get RequestLog by requestId"

# AUDIT LOG TESTS
echo -e "${YELLOW}=== AUDIT LOG TESTS ===${NC}" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

test_endpoint 4 "POST" "/api/logs/audit" "Create AuditLog" \
  '{"action":"CREATED","entityType":"SERVICE","entityId":"svc-001","userId":"admin","timestamp":"'$(date -u +%Y-%m-%dT%H:%M:%S.000Z)'","details":"Service created"}'

test_endpoint 5 "GET" "/api/logs/audit" "Get all AuditLogs"

test_endpoint 6 "GET" "/api/logs/audit/entity/SERVICE/svc-001" "Get AuditLogs by entity"

# CIRCUIT BREAKER TESTS
echo -e "${YELLOW}=== CIRCUIT BREAKER TESTS ===${NC}" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

test_endpoint 7 "GET" "/circuit-breaker/states" "Get circuit breaker states"

test_endpoint 8 "GET" "/circuit-breaker/state/UserService" "Get circuit breaker state"

test_endpoint 9 "GET" "/circuit-breaker/allow/UserService" "Check if request allowed"

# SERVICE INSTANCE TESTS
echo -e "${YELLOW}=== SERVICE INSTANCE TESTS ===${NC}" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

test_endpoint 10 "GET" "/service-instances" "Get all service instances"

test_endpoint 11 "GET" "/service-instances/service/UserService" "Get service instances by name"

# SUMMARY
echo "" | tee -a "$LOG_FILE"
echo "=========================================="  | tee -a "$LOG_FILE"
echo "Test Summary"                               | tee -a "$LOG_FILE"
echo "=========================================="  | tee -a "$LOG_FILE"
echo -e "${GREEN}Passed: $PASSED${NC}"             | tee -a "$LOG_FILE"
echo -e "${RED}Failed: $FAILED${NC}"               | tee -a "$LOG_FILE"
TOTAL=$((PASSED + FAILED))
echo "Total: $TOTAL"                               | tee -a "$LOG_FILE"
if [ $TOTAL -gt 0 ]; then
  PERCENTAGE=$((PASSED * 100 / TOTAL))
  echo "Success Rate: ${PERCENTAGE}%"              | tee -a "$LOG_FILE"
fi
echo "=========================================="  | tee -a "$LOG_FILE"
echo "Log: $LOG_FILE"                              | tee -a "$LOG_FILE"
echo ""                                             | tee -a "$LOG_FILE"

if [ $FAILED -eq 0 ]; then
  echo -e "${GREEN}✓ All tests passed!${NC}"       | tee -a "$LOG_FILE"
  exit 0
else
  echo -e "${RED}✗ $FAILED test(s) failed${NC}"    | tee -a "$LOG_FILE"
  exit 1
fi
