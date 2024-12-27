FROM maven:3.9-amazoncorretto-23 AS  builder
WORKDIR /app
COPY ./pom.xml /app/pom.xml
COPY ./src /app/src
RUN mvn -f /app/pom.xml clean package -DskipTests


FROM amazoncorretto:23
VOLUME /tmp
ARG BUILDER_DIR=app
ARG APPJAR=target/*.jar
COPY --from=builder ${BUILDER_DIR}/${APPJAR} /app/app.jar
ENV TZ=Europe/Samara
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]