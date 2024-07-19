FROM amazoncorretto:17

WORKDIR /app

COPY ./build/libs/yumyum-0.0.1-SNAPSHOT.jar .

COPY ./src/main/resources/springkey.p12 ./src/main/resources/springkey.p12

CMD ["java", "-jar", "yumyum-0.0.1-SNAPSHOT.jar"]
