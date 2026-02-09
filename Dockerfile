#FROM eclipse-temurin:17-jdk-alpine
#
#WORKDIR /app
#
#COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java","-jar","app.jar"]
#
## Wait for MySQL to be ready before starting the app
#CMD ["/app/wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

RUN apk add --no-cache maven

EXPOSE 8080
