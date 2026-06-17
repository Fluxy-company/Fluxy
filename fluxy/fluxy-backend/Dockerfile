FROM maven:3.9.11-amazoncorretto-21 AS deps

WORKDIR /app

COPY pom.xml .
COPY .mvn/ .mvn/
RUN mvn dependency:go-offline -B

FROM deps AS dev

WORKDIR /app

COPY . .

ENTRYPOINT ["mvn", "spring-boot:run", \
    "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", \
    "-Dspring.devtools.restart.enabled=true", \
    "-Dspring.devtools.livereload.enabled=true"]