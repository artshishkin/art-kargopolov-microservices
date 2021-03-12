package net.shyshkin.study.photoapp.api.users.ui.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.photoapp.api.users.services.UserService;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import net.shyshkin.study.photoapp.api.users.ui.model.CreateUserRequestModel;
import net.shyshkin.study.photoapp.api.users.ui.model.CreateUserResponseModel;
import net.shyshkin.study.photoapp.api.users.ui.model.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.function.Supplier;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@Slf4j
@RestController
@RequestMapping(UsersController.BASE_URL)
@RequiredArgsConstructor
public class UsersController {

    static final String BASE_URL = "/users";

    private final Environment environment;
    private final UserService userService;
    private final Supplier<ModelMapper> modelMapperFactory;

    @GetMapping("/status/check")
    public String status() {
        String appName = environment.getProperty("spring.application.name");
        Integer port = environment.getProperty("local.server.port", Integer.TYPE);
        String tokenSecret = environment.getProperty("token.secret");
        return String.format("%s is running on port: %s. Token secret: %s", appName, port, tokenSecret);
    }

    @PostMapping(
            consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE},
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponseModel createUser(@Valid @RequestBody CreateUserRequestModel user) {
        ModelMapper mapper = modelMapperFactory.get();

        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto userDtoCreated = userService.createUser(userDto);
        return mapper.map(userDtoCreated, CreateUserResponseModel.class);
    }

    @GetMapping(value = "{userId}", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    @PreAuthorize("#userId == principal")
    public UserResponseModel getUser(@PathVariable("userId") UUID userId) {
        UserDto userDto = userService.getUserDetailsByUserId(userId);

        ModelMapper mapper = modelMapperFactory.get();

        UserResponseModel userResponseModel = mapper.map(userDto, UserResponseModel.class);
        log.debug("Found User: {}", userResponseModel);
        return userResponseModel;
    }
}
