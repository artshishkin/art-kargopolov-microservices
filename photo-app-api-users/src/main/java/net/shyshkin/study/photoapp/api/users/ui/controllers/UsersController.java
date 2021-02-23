package net.shyshkin.study.photoapp.api.users.ui.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final Environment environment;

    @GetMapping("/status/check")
    public String status() {
        return environment.getProperty("spring.application.name") + " is running on port: " + environment.getProperty("local.server.port");
    }
}
