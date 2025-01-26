FROM openjdk:21

WORKDIR /app

COPY target/ansh-service-1.0.jar /app/asch.jar

EXPOSE 8443

ENTRYPOINT ["java", "--enable-preview", "-jar", "/app/asch.jar"]
