FROM openjdk:8
ADD /grocerica-api.jar grocerica-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "grocerica-api.jar"]
