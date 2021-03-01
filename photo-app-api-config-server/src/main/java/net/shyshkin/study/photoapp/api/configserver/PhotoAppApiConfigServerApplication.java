package net.shyshkin.study.photoapp.api.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class PhotoAppApiConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoAppApiConfigServerApplication.class, args);
    }

}
