FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/ms-stats-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "-jar", "app.jar"]
