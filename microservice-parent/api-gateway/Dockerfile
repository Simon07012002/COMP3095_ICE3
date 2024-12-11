#--------
#Buid stage
#--------

FROM gradle:8-jdk21-alpine AS builder

COPY --chown=gradle:gradle . /home/gradle/src/

WORKDIR /home/gradle/src

RUN gradle build -x test

#--------
#Package stage
#--------
FROM openjdk:21-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/api-gateway.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]
