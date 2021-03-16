package net.shyshkin.study.photoapp.api.configserver.postprocessors;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
public class DockerSecretsPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        String bindPathProperty = environment.getProperty("docker-secret.bind-path", "/run/secrets");

        log.debug("value of `docker-secret.bind-path` property: `{}`", bindPathProperty);

        Path bindPath = Paths.get(bindPathProperty);
        if (Files.isDirectory(bindPath)) {
            Map<String, Object> dockerSecrets;
            try {
                dockerSecrets = Files
                        .list(bindPath)
                        .collect(
                                toMap(
                                        path -> "docker-secret-" + path.toFile().getName(),
                                        this::readString
                                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            dockerSecrets.forEach((key, value) -> log.debug("{} = `{}`", key, value));

            MapPropertySource pptySource = new MapPropertySource("docker-secrets", dockerSecrets);

            environment.getPropertySources().addLast(pptySource);
        }
    }

    @SneakyThrows(IOException.class)
    private String readString(Path path) {
        return Files.readString(path);
    }
}
