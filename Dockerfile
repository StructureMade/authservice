FROM openjdk:15
ADD target/authservice.jar authservice.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "authservice.jar"]