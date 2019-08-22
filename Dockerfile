FROM openjdk:8
MAINTAINER Radiant Digital
ADD target/*.jar /msa-deposit-service.jar
RUN bash -c 'touch /msa-deposit-service.jar'
CMD ["java","-Dspring.profiles.active=docker","-jar","/msa-deposit-service.jar"]
EXPOSE 4444
