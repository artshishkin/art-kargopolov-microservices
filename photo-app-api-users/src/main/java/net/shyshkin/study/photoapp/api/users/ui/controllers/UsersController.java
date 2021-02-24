package net.shyshkin.study.photoapp.api.users.ui.controllers;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.photoapp.api.users.services.UserService;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import net.shyshkin.study.photoapp.api.users.ui.model.CreateUserRequestModel;
import net.shyshkin.study.photoapp.api.users.ui.model.CreateUserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

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

    @PostMapping(
            consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE},
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
            )
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponseModel createUser(@Valid @RequestBody CreateUserRequestModel user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto userDtoCreated = userService.createUser(userDto);
        return mapper.map(userDtoCreated, CreateUserResponseModel.class);
    }
}
