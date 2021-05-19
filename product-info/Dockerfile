FROM openjdk:8-jdk-alpine
EXPOSE 8084
ARG JAR_FILE=target/product-info-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE}  productinfo.jar
RUN java -Djarmode=layertools -jar productinfo.jar extract
ENTRYPOINT ["java","-jar","/productinfo.jar"]