FROM amazoncorretto:17

WORKDIR /app

COPY ./build/libs/yumyum-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "yumyum-0.0.1-SNAPSHOT.jar"]
