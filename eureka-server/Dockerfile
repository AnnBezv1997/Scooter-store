FROM openjdk:8-jdk-alpine
EXPOSE 8761
ARG JAR_FILE=target/eureka-server-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE}  eureka.jar
RUN java -Djarmode=layertools -jar eureka.jar extract
ENTRYPOINT ["java","-jar","/eureka.jar"]