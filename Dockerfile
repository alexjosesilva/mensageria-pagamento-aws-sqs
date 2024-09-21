# Etapa 1: Construir o projeto
FROM gradle:7.6.0-jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon

# Etapa 2: Criar a imagem final
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Expor a porta que o Spring Boot usa
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]