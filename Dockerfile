FROM amazoncorretto:11

WORKDIR /app

COPY . .

RUN ./gradlew build

VOLUME /tmp

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "build/libs/currency-checker-0.0.1-SNAPSHOT.jar"]