FROM gradle:7.2-jdk17 AS builder

LABEL maintainer ="bianavic"

WORKDIR /app

#COPY build/libs/calculator-0.0.1-SNAPSHOT.jar /app/calculator.jar
COPY . .

RUN ./gradlew build

FROM openjdk:17

COPY --from=builder /app/build/libs/calculator-0.0.1-SNAPSHOT.jar /app/calculator-0.0.1-SNAPSHOT.jar

EXPOSE 8001

#CMD ["java","-jar","calculator.jar"]
CMD ["java", "-jar", "/app/calculator-0.0.1-SNAPSHOT.jar"]