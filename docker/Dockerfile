FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

ARG GITHUB_ACTOR=jho951
ARG GH_TOKEN
ENV GITHUB_ACTOR=${GITHUB_ACTOR}
ENV GH_TOKEN=${GH_TOKEN}

COPY gradlew settings.gradle build.gradle gradle.properties ./
COPY gradle ./gradle
COPY common ./common
COPY app ./app

RUN chmod +x ./gradlew && ./gradlew clean :app:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /workspace/app/build/libs/*.jar app.jar

ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
