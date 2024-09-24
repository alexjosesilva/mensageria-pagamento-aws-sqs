# Etapa 1: Build da aplicação
FROM gradle:7.6.0-jdk17 AS build
WORKDIR /app

# Copia os arquivos de build.gradle e settings.gradle da raiz
COPY build.gradle settings.gradle /app/

# Copia o código-fonte da aplicação
COPY san-giorgio-api/src /app/src

# Baixa as dependências do Gradle e compila o projeto
RUN gradle build -x test --no-daemon

# Gera o JAR
RUN gradle bootJar --no-daemon

# Verifica se o JAR foi gerado
RUN ls /app/build/libs/

# Etapa 2: Criação da imagem final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Define a variável de ambiente AWS_REGION
ENV AWS_REGION=us-east-2

# Copia o JAR gerado na etapa de build
COPY --from=build /app/build/libs/san-giorgio-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que a aplicação irá rodar
EXPOSE 8081
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
