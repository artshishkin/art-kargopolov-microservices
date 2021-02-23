package net.shyshkin.study.photoapp.api.users.ui.controllers;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.photoapp.api.users.services.UserService;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import net.shyshkin.study.photoapp.api.users.ui.model.CreateUserRequestModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(UsersController.BASE_URL)
@RequiredArgsConstructor
public class UsersController {

    static final String BASE_URL = "/users";

    private final Environment environment;
    private final UserService userService;

    @GetMapping("/status/check")
    public String status() {
        return environment.getProperty("spring.application.name") + " is running on port: " + environment.getProperty("local.server.port");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody CreateUserRequestModel user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        return userService.createUser(userDto);
    }
}
