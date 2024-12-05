FROM openjdk:17-jdk-alpine

# Definindo variáveis de ambiente diretamente, se necessário
ENV DB_SERVER=mysql-container
ENV DB_NAME=estoque
ENV DB_USERNAME=estoque
ENV DB_PASSWORD=estoque
ENV APP_PROFILE=prod
ENV CLIENT_ID=clientid
ENV CLIENT_SECRET=clientsecret
ENV JWT_DURATION=86400
ENV CORS_ORIGINS=localhost:3000

COPY /estoque/target/estoque-0.0.1-SNAPSHOT.jar estoque.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/estoque.jar"]
