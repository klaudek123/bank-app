# Stage 1: Build the Java application
FROM maven:3.8.7-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ ./src/
RUN mvn clean package -DskipTests

# Stage 2: Build the final image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/backend-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend-app.jar"]
