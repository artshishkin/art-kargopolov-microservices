package net.shyshkin.study.photoapp.api.users.ui.controllers;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.photoapp.api.users.ui.model.CreateUserRequestModel;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final Environment environment;

    @GetMapping("/status/check")
    public String status() {
        return environment.getProperty("spring.application.name") + " is running on port: " + environment.getProperty("local.server.port");
    }

    @PostMapping
    public String createUser(@Valid @RequestBody CreateUserRequestModel user) {
        return "Create user method is Called for user" + user;
    }
}
