# Step 1: Build da aplicação via Maven com JDK 21 (ou a versão utilizada no seu projeto)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia dependências e faz o download offline
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia código fonte e gera o pacote .jar
COPY src ./src
RUN mvn package -DskipTests

# Step 2: Imagem final leve (JRE) para rodar o app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o .jar gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]