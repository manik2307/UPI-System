FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/bank-a-1.0.0.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]