FROM maven:3.8.5-openjdk-17 AS BUILD
COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /taget/demo-0.0.1-SNAPSHOT.jar weatherapp.jar
ENV PORT 8080
EXPOSE 8080
CMD ["java", "-jar", "weatherapp.jar", "--server.port=${PORT}"]