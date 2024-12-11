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

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/inventory-service.jar

# Set environment variables for PostgreSQL
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/inventory-service \
    SPRING_DATASOURCE_USERNAME=admin \
    SPRING_DATASOURCE_PASSWORD=password
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]
