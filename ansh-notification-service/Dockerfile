FROM openjdk:21

WORKDIR /app

COPY target/ansh-notification-service-1.0.jar /app/ansh-notification-service.jar

EXPOSE 8444

ENTRYPOINT ["java", "--enable-preview", "-jar", "/app/ansh-notification-service.jar"]
