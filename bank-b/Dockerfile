FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/bank-b-1.0.0.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]