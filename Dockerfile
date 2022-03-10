FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD /build/libs/kotlintest* app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]