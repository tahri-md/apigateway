# Multi-stage build
FROM maven:3.9-Eclipse-Temurin-21 AS builder

WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM Eclipse-Temurin:21-jre

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /build/target/apigateway-*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
