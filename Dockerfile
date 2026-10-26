#
# Build stage
#
FROM maven:3.9-eclipse-temurin-21-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /home/app/target/recette-1.0.0.jar /usr/local/lib/recette.jar
EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["java","-jar","/usr/local/lib/recette.jar"]
