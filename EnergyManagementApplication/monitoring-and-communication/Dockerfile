FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

RUN ls -la /app/target

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/monitoring-and-communication-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
