FROM openjdk:21
VOLUME /tmp
COPY target/gr-1.jar gr.jar
ENTRYPOINT ["java","-jar","/gr.jar"]