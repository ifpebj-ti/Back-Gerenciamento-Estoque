FROM openjdk:17-jdk-alpine

COPY /estoque/target/estoque-0.0.1-SNAPSHOT.jar estoque.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/estoque.jar"]