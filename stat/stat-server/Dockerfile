FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar app.jar
ARG JAR_FILE
ENTRYPOINT ["java","-jar","/app.jar"]
