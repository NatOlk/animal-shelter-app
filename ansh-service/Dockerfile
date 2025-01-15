FROM openjdk:21

COPY target/ansh-service-1.0.jar /asch.jar

EXPOSE 8443

ENTRYPOINT ["java", "--enable-preview", "-jar", "/asch.jar"]
