FROM amazoncorretto:19 as builder
WORKDIR /home/ubuntu/application
ARG JAR_FILE=target/lab1-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
LABEL authors="Михаил Дюсов ИУ7-11М"

FROM amazoncorretto:19
ENV PORT=8080
WORKDIR /home/ubuntu/application
COPY --from=builder /home/ubuntu/application/spring-boot-loader/ ./
COPY --from=builder /home/ubuntu/application/dependencies/ ./
COPY --from=builder /home/ubuntu/application/snapshot-dependencies/ ./
COPY --from=builder /home/ubuntu/application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]