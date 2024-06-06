FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install -y openjdk-17-jdk maven
COPY . .
RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /target/Tutienda-0.0.1-SNAPSHOT.jar /app.jar

# Comando para ejecutar tu aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "/app.jar"]

