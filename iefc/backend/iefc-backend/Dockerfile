FROM maven:3.9.11-amazoncorretto-21 AS deps

WORKDIR /app

COPY pom.xml .
COPY .mvn/ .mvn/
RUN mvn dependency:go-offline -B

# ---------- DEV (hot reload + remote debug, usado pelo docker-compose.yml) ----------
FROM deps AS dev

WORKDIR /app

COPY . .

ENTRYPOINT ["mvn", "spring-boot:run", \
    "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", \
    "-Dspring.devtools.restart.enabled=true", \
    "-Dspring.devtools.livereload.enabled=true"]

# ---------- BUILD (gera o jar para produção) ----------
FROM deps AS build

WORKDIR /app

COPY . .
RUN mvn clean package -DskipTests -B

# ---------- PROD (imagem final enxuta, usada pelo docker-compose.prod.yml) ----------
FROM amazoncorretto:21-alpine AS prod

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /app/target/*.jar app.jar
RUN chown spring:spring app.jar
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
