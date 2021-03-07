FROM openjdk:11-jre-slim as builder
WORKDIR application
ADD maven/${project.build.finalName}.jar ${project.build.finalName}.jar
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

FROM openjdk:11-jre-slim

RUN apt update && apt install -y curl

HEALTHCHECK  --interval=5s --timeout=2s --start-period=20s --retries=5 \
    CMD curl -f localhost:8012/actuator/health || false

LABEL PROJECT_NAME=${project.artifactId} \
      PROJECT=${project.id}

EXPOSE 8012

ENV SPRING_PROFILES_ACTIVE=git,asymmetric
#ENV SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD=ProvidePasswordThroughEnvironmentVariable
ENV SPRING_RABBITMQ_HOST=rabbit-mq
ENV ENCRYPT_KEY_STORE_LOCATION=file://apiEncryptionKey.keystore.jks

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
COPY apiEncryptionKey.keystore.jks ./
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]