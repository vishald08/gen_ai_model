# Stage 1: Build the WAR/JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Explode the package and Run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Install unzip to explode the archive
RUN apk add --no-cache unzip

# Copy build artifact
COPY --from=build /app/target/*.[jw]ar /app/app.war

# Extract WAR/JAR into /app/exploded directory
RUN mkdir /app/exploded && unzip -q /app/app.war -d /app/exploded

EXPOSE 8080

# Run Spring Boot in Exploded Mode
WORKDIR /app/exploded
ENTRYPOINT ["java", "-cp", ".:WEB-INF/classes:WEB-INF/lib/*", "org.springframework.boot.loader.launch.WarLauncher"]