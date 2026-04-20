# syntax=docker/dockerfile:1.7

FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

ARG GITHUB_ACTOR=jho951
ENV GITHUB_ACTOR=${GITHUB_ACTOR}

COPY gradlew settings.gradle build.gradle gradle.properties ./
COPY gradle ./gradle
COPY common ./common
COPY app ./app

RUN --mount=type=secret,id=gh_token \
    chmod +x ./gradlew && \
    GH_TOKEN="$(cat /run/secrets/gh_token 2>/dev/null || true)" \
    ./gradlew clean :app:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /workspace/app/build/libs/*.jar app.jar

ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
