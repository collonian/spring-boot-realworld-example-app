#build stage
FROM gradle:jdk-alpine as build-stage

WORKDIR /home/gradle/project

USER root

RUN apk update

ENV GRADLE_USER_HOME /home/gradle/project

COPY . /home/gradle/project

RUN ./gradlew build

#production stage
FROM java:jre-alpine

WORKDIR /home/gradle/project

EXPOSE 8080

COPY --from=build-stage /home/gradle/project/build/libs/project-0.0.1-SNAPSHOT.jar .

ENTRYPOINT java $JAVA_OPTS -jar project-0.0.1-SNAPSHOT.jar
