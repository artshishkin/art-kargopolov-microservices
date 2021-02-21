package net.shyshkin.study.app.ws.ui.controllers;

import net.shyshkin.study.app.ws.ui.model.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/users";

    @GetMapping
    public String getAllUser(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "25") int limit,
                             @RequestParam(required = false) String sort) {
        return String.format("Get Users was called. Page %d of %d. Sort is %s", page, limit, sort);
    }

    @GetMapping(value = "/{userId}", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public UserDto getUser(@PathVariable String userId) {
        return UserDto.builder()
                .userId(userId)
                .firstName("Art")
                .lastName("Shyshkin")
                .email("myemail@example.com")
                .build();
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
