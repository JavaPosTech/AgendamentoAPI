FROM gradle:jdk21-alpine AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine-3.22

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar RestauranteAPI.jar

ENTRYPOINT ["java", "-jar", "RestauranteAPI.jar"]