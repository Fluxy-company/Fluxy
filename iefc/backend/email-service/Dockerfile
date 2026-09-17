FROM maven:3.9.11-amazoncorretto-21 AS deps

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

FROM deps AS dev

WORKDIR /app

COPY . .

ENTRYPOINT ["mvn", "spring-boot:run", \
    "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006", \
    "-Dspring.devtools.restart.enabled=true", \
    "-Dspring.devtools.livereload.enabled=true"]

FROM deps AS build

WORKDIR /app

COPY . .
RUN mvn clean package -DskipTests -B

FROM amazoncorretto:21-alpine AS prod

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /app/target/*.jar app.jar
RUN chown spring:spring app.jar
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
