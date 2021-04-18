FROM openjdk:8-jdk-alpine
MAINTAINER symbizsolutions.com
COPY build/libs/AWSMicroServiceDemo-1.0-SNAPSHOT-boot.jar AWSMicroServiceDemo-1.0-SNAPSHOT-boot.jar
ENTRYPOINT ["java","-jar","/AWSMicroServiceDemo-1.0-SNAPSHOT-boot.jar"]
