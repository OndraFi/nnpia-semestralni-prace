# Stage 1: Build aplikace
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
# Kopírování wrapperu a konfigurace gradle
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./
# Stažení závislostí (optimalizace cache)
RUN ./gradlew dependencies --no-daemon

# Kopírování zdrojových kódů a sestavení
COPY src/ src/
RUN ./gradlew clean bootJar --no-daemon -x test

# Stage 2: Runtime aplikace
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Kopírování vygenerovaného jar souboru
COPY --from=build /app/build/libs/*.jar app.jar
# Exponování portu aplikace
EXPOSE 9000
# Spuštění
ENTRYPOINT ["java", "-jar", "app.jar"]
