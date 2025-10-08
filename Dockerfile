# --- Stage 1: Build the application ---
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml ./
COPY jlmap-api ./jlmap-api
COPY jlmap-vaadin ./jlmap-vaadin
COPY jlmap-vaadin-demo ./jlmap-vaadin-demo

RUN mvn -f jlmap-vaadin-demo/pom.xml clean package -Pproduction

# --- Stage 2: Create the final image ---
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/jlmap-vaadin-demo/target/jlmap-vaadin-demo-2.0.0.jar app.jar
EXPOSE 80
ENTRYPOINT ["java", "-Dserver.port=80", "-jar", "app.jar"]
