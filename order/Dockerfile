FROM openjdk:8-jdk-alpine
EXPOSE 8086
ARG JAR_FILE=target/order-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE}  order.jar
RUN java -Djarmode=layertools -jar order.jar extract
ENTRYPOINT ["java","-jar","/order.jar"]