FROM openjdk:21 AS base

WORKDIR /app

COPY build/libs/*.jar app.jar

RUN ["chmod", "+x", "app.jar"]

EXPOSE 8080

FROM postgres

# Your Spring Boot application service definition
FROM base
COPY src/main/resources/application.yml .

ENTRYPOINT ["java", "-jar", "app.jar"]
