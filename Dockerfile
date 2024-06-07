FROM maven:3.8.5-openjdk-17-slim AS build

# Copia el archivo POM y descarga las dependencias del proyecto
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el resto del código fuente y construye el proyecto
COPY src ./src
RUN mvn package -DskipTests

# Etapa 2: Ejecución
FROM openjdk:17-jdk-slim

# Exponer el puerto de la aplicación
EXPOSE 8080

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /target/tucalzado-0.0.1-SNAPSHOT.jar /app.jar

# Comando para ejecutar la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]