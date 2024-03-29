FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY ../../../minimal_api/ /app
WORKDIR /app
RUN mvn clean && mvn package


FROM openjdk:21
COPY --from=build /app/target/minimal_api-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]