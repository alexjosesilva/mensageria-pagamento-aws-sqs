# Etapa 1: Build da aplicação
FROM gradle:7.6.0-jdk17 AS build
WORKDIR /app

# Copia os arquivos de build.gradle e settings.gradle (se houver)
COPY build.gradle /app/
COPY settings.gradle /app/  # Se você tiver um arquivo settings.gradle

# Baixa as dependências do Gradle
RUN gradle build --no-daemon || return 0

# Copia o código-fonte da aplicação
COPY . /app

# Compila o projeto e gera o JAR
RUN gradle bootJar --no-daemon

# Etapa 2: Criação da imagem final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia o JAR gerado na etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Expõe a porta que a aplicação irá rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]