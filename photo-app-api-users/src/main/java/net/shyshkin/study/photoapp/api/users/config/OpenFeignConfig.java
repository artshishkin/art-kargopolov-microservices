package net.shyshkin.study.photoapp.api.users.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "net.shyshkin.study.photoapp.api.users.services")
public class OpenFeignConfig {
}
