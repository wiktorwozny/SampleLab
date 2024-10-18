# Etap 1: Budowanie aplikacji
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Ustawienie katalogu roboczego w kontenerze
WORKDIR /app

# Skopiowanie pliku pom.xml i wszystkich zależności Maven
COPY pom.xml ./
COPY src ./src
COPY .env ./

# Budowanie aplikacji
RUN mvn clean package -DskipTests

# Etap 2: Uruchomienie aplikacji
FROM eclipse-temurin:17-jdk-alpine

# Katalog roboczy dla aplikacji
WORKDIR /app

# Skopiowanie skompilowanej aplikacji z poprzedniego etapu
COPY --from=build /app/target/*.jar app.jar

# Uruchomienie aplikacji Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]