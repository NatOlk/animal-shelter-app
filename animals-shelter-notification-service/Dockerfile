FROM openjdk:21
VOLUME /tmp
COPY target/ansh-notification-service-1.jar ansh-notification-service.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/ansh-notification-service.jar"]