FROM openjdk:18
MAINTAINER angelolamonaca.com
ARG WAR_FILE=build/libs/AIU-Auth-Service-0.0.1-SNAPSHOT.war
COPY ${WAR_FILE} app.war
EXPOSE 3020
ENTRYPOINT ["java","-jar","/app.war"]
