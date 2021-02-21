package net.shyshkin.study.app.ws.ui.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/users";

    @GetMapping("/{userId}")
    public String getUser(@PathVariable String userId) {
        return "Get User was called for user " + userId;
    }

    @PostMapping
    public String createUser(String user) {
        return "Was created user: " + user;
    }

    @PutMapping
    public String updateUser(String user) {
        return "User was updated: " + user;
    }

    @DeleteMapping
    public String deleteUser(String user) {
        return "User was deleted: " + user;
    }
}
