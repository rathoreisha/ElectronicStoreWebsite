FROM openjdk:8
EXPOSE 8080
ADD target/ElectronicStored-0.0.1-SNAPSHOT.jar ElectronicStored-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ElectronicStored-0.0.1-SNAPSHOT.jar"]