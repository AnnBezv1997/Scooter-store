FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=target/gateway-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE}  gateway.jar
RUN java -Djarmode=layertools -jar gateway.jar extract
ENTRYPOINT ["java","-jar","/gateway.jar"]