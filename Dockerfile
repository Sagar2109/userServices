FROM openjdk:21-jdk-slim

MAINTAINER sagar.com

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
