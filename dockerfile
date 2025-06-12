FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle

RUN ./gradlew dependencies

COPY src ./src

RUN ./gradlew build -x test

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/rate-limiter-*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]