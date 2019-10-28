FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD 'top.techial-knowledge-api.jar' 'app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar", "--spring.profiles.active=docker"]
