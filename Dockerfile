FROM amazoncorretto:17-alpine3.17-full
WORKDIR /app
COPY . /app
RUN ./gradlew clean build
RUN chmod +x run_docker.sh
ENTRYPOINT ["sh", "run_docker.sh"]