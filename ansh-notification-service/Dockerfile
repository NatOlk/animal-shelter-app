FROM openjdk:21

COPY target/ansh-notification-service-1.0.jar /ansh-notification-service.jar

EXPOSE 8444

ENTRYPOINT ["java", "--enable-preview", "-jar", "/ansh-notification-service.jar"]
