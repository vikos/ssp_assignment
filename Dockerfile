FROM gradle:8.10.2-jdk21 AS build
LABEL authors="vikos"

COPY --chown=gradle:gradle . /src

WORKDIR /src
RUN gradle fatJar
RUN ls -lah /src/build/libs

FROM openjdk:24-jdk-slim
COPY --from=build /src/build/libs/ssp_assignment-1.0-SNAPSHOT-all.jar /app/ssp_assignment.jar

VOLUME /data
CMD java -jar /app/ssp_assignment.jar -d /data/input.csv

