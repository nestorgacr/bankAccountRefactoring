FROM openjdk:21
COPY ./target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8086
CMD ["java", "-jar", "app.jar"]