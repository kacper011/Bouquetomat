FROM openjdk:21-jdk



WORKDIR /app

# Skopiuj plik JAR aplikacji do kontenera
COPY target/bouquetomat-0.0.1-SNAPSHOT.jar bouquetomat.jar

# Ustawienie portu, który będzie eksponowany na zewnątrz kontenera
EXPOSE 8080

# Uruchom aplikację
ENTRYPOINT ["java", "-jar", "bouquetomat.jar"]
