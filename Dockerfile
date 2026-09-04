# --- Etapa 1: build ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# --- Etapa 2: runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

# Variables de entorno requeridas en runtime (dev y prod tendran valores distintos):
# AZURE_TENANT_ID, AZURE_CLIENT_ID, FRONTEND_URL, DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
