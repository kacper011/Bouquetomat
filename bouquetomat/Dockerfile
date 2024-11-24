FROM openjdk:21-jdk

WORKDIR /app

COPY target/bouquetomat-0.0.1-SNAPSHOT.jar bouquetomat.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "bouquetomat.jar"]
