# Stage 1: Build JAR/WAR using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run Application using lightweight JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Ye line target folder ke andar se *.jar ya *.war jo bhi bane use app.jar bana degi
COPY --from=build /app/target/*.[jw]ar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]