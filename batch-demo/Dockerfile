FROM maven:3.5.4-jdk-8-alpine AS maven-build
WORKDIR /app
add . /app
RUN cd /app && mvn install

FROM java:8u111-jre-alpine
RUN apk update && apk add ca-certificates && rm -rf /var/cache/apk/*
WORKDIR /app
COPY --from=maven-build /app/target/*.jar /app

EXPOSE 8080
ENTRYPOINT java -jar batch-demo-1.1.2-RELEASE.jar