FROM openjdk:17

LABEL maintainer ="bianavic"

WORKDIR /app

COPY build/libs/calculator-0.0.1-SNAPSHOT.jar /app/calculator.jar

EXPOSE 8001

CMD ["java","-jar","calculator.jar"]