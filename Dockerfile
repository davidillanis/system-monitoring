
#FROM amazoncorretto:17-alpine-jdk
#COPY target/system-monitoring-collector-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY . .

RUN ./mvnw clean package

CMD ["java", "-jar", "target/system-monitoring-collector-0.0.1-SNAPSHOT.jar"]

