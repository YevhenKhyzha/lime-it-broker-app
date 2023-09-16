# https://spring.io/guides/topicals/spring-boot-docker/

FROM openjdk:17-jdk-slim-buster

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=lime-it-broker-boot/target/lime-it-broker-app-executable.jar

COPY ${JAR_FILE} lime-it-broker-app-executable.jar

ENTRYPOINT ["java","-jar","/lime-it-broker-app-executable.jar"]