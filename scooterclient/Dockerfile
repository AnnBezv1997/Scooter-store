FROM openjdk:8-jdk-alpine
EXPOSE 8090
ARG JAR_FILE=target/scooter-client-1.0-SNAPSHOT.jar
ADD ${JAR_FILE}  client.jar
RUN java -Djarmode=layertools -jar client.jar extract
ENTRYPOINT ["java","-jar","/client.jar"]