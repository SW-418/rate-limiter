FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle

RUN ./gradlew dependencies

COPY src ./src

RUN ./gradlew build -x test

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/rate-limiter-*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]