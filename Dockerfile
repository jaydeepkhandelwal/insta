FROM openjdk:8-jdk-alpine

COPY build/libs/insta-0.0.1-SNAPSHOT.jar server.jar
CMD java -XX:+PrintFlagsFinal  -jar server.jar