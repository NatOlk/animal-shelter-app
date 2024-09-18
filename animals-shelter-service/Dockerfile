FROM openjdk:21
VOLUME /tmp
COPY target/ansh-1.jar asch.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/asch.jar"]