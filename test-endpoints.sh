#!/bin/bash

# API Gateway Endpoints Testing Script
# This script tests all critical API Gateway endpoints
# Usage: ./test-endpoints.sh
# Prerequisites: curl and apigateway service running on localhost:8080
#

BASE_URL="http://localhost:8080"
LOG_FILE="test-results-$(date +%Y%m%d-%H%M%S).log"

# Counters
PASSED=0
FAILED=0
TOTAL=0

echo "======================================"
echo "API Gateway Endpoint Tests"
echo "======================================"
echo "Results logged to: $LOG_FILE"
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to test an endpoint
test_endpoint() {
    local test_num=$1
    local test_name=$2
    local method=$3
    local endpoint=$4
    
    TOTAL=$((TOTAL + 1))
    
    echo -e "${YELLOW}[Test $test_num] $test_name${NC}" | tee -a "$LOG_FILE"
    echo "URL: $method $BASE_URL$endpoint" | tee -a "$LOG_FILE"
    
    # Perform the request and capture response
    http_code=$(curl -s -o /tmp/response.json -w "%{http_code}" -X "$method" "$BASE_URL$endpoint" \
      -H "Content-Type: application/json")
    
    echo "HTTP Status: $http_code" | tee -a "$LOG_FILE"
    
    # Check if status is 2xx or 3xx (success)
    if [[ $http_code =~ ^[23][0-9]{2}$ ]]; then
        echo -e "${GREEN}✓ PASSED${NC}" | tee -a "$LOG_FILE"
        PASSED=$((PASSED + 1))
        # Show response preview
        if [ -s /tmp/response.json ]; then
            echo "Response preview:" | tee -a "$LOG_FILE"
            head -c 200 /tmp/response.json | tee -a "$LOG_FILE"
            echo "..." | tee -a "$LOG_FILE"
        fi
    else
        echo -e "${RED}✗ FAILED${NC}" | tee -a "$LOG_FILE"
        FAILED=$((FAILED + 1))
        # Show error response
        if [ -s /tmp/response.json ]; then
            echo "Error response:" | tee -a "$LOG_FILE"
            cat /tmp/response.json | tee -a "$LOG_FILE"
        fi
    fi
    
    echo "" | tee -a "$LOG_FILE"
    echo "" | tee -a "$LOG_FILE"
}

# Test 1: Get request log
test_endpoint 1 "Get Request Log" GET "/api/logs/requests/test-request-123"

# Test 2: Get requests by user
test_endpoint 2 "Get Requests by User" GET "/api/logs/requests/user/user-001?limit=10&offset=0"

# Test 3: Get error requests
test_endpoint 3 "Get Error Requests" GET "/api/logs/requests/errors?startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59"

# Test 4: Get audit log by ID
test_endpoint 4 "Get Audit Log by ID" GET "/api/logs/audit/550e8400-e29b-41d4-a716-446655440000"

# Test 5: Get entity history
test_endpoint 5 "Get Entity History" GET "/api/logs/audit/entity-history?entityType=Route&entityId=route-123"

# Test 6: Get changes by action
test_endpoint 6 "Get Changes by Action" GET "/api/logs/audit/by-action/CREATE?startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59"

# Test 7: Get audit summary
test_endpoint 7 "Get Audit Summary" GET "/api/logs/audit/summary?startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59"

# Test 8: Get slow requests
test_endpoint 8 "Get Slow Requests" GET "/api/logs/requests/slow?thresholdMs=1000&limit=10"

# Test 9: Get status distribution
test_endpoint 9 "Get Status Code Distribution" GET "/api/logs/requests/status-distribution?startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59"

# Summary
echo "======================================"
echo "TEST SUMMARY"
echo "======================================"
echo "Total Tests: $TOTAL" | tee -a "$LOG_FILE"
echo -e "${GREEN}Passed: $PASSED${NC}" | tee -a "$LOG_FILE"
echo -e "${RED}Failed: $FAILED${NC}" | tee -a "$LOG_FILE"

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}All tests passed!${NC}" | tee -a "$LOG_FILE"
else
    echo -e "${RED}Some tests failed. Check $LOG_FILE for details.${NC}" | tee -a "$LOG_FILE"
fi

echo "" | tee -a "$LOG_FILE"
echo "Full results saved to: $LOG_FILE"

# Cleanup
rm -f /tmp/response.json
