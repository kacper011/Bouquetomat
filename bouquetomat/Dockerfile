# Użyj oficjalnej wersji Javy jako obrazu bazowego
FROM openjdk:21-jdk

# Określ katalog roboczy w kontenerze
WORKDIR /app

# Skopiuj plik JAR aplikacji do kontenera
COPY target/bouquetomat-0.0.1-SNAPSHOT.jar bouquetomat.jar

# Ustawienie portu, który będzie eksponowany na zewnątrz kontenera
EXPOSE 8080

# Uruchom aplikację
ENTRYPOINT ["java", "-jar", "bouquetomat.jar"]
