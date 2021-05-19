FROM openjdk:8-jdk-alpine
EXPOSE 8085
ARG JAR_FILE=target/stock-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE}  stockservice.jar
RUN java -Djarmode=layertools -jar stockservice.jar extract
ENTRYPOINT ["java","-jar","/stockservice.jar"]