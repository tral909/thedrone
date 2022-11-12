FROM adoptopenjdk/openjdk11:alpine-jre
MAINTAINER Roman Egorov <indorm1992@mail.ru>
COPY build/libs/thedrone-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]