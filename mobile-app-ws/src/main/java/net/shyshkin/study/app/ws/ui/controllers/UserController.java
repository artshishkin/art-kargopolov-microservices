package net.shyshkin.study.app.ws.ui.controllers;

import net.shyshkin.study.app.ws.ui.model.UserDetails;
import net.shyshkin.study.app.ws.ui.model.dto.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/users";

    private final Map<UUID, UserDto> userRepository = new HashMap<>();

    @GetMapping
    public List<UserDto> getAllUser(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "25") int limit,
                                    @RequestParam(required = false) String sort) {
        return userRepository.values()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{userId}", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ArtHeader", "SuperSecretArtHeaderValue");

        return Optional
                .ofNullable(userRepository.get(userId))
                .map(userDto -> new ResponseEntity<>(userDto, httpHeaders, 200))
                .orElse(ResponseEntity.noContent().build());
    }

//curl --location --request POST 'http://localhost:8080/users' \
//--header 'Accept: application/json' \
//--header 'Content-Type: application/json' \
//--header 'Cookie: JSESSIONID=DDE78460CC129994CDBE87744FE56B89' \
//--data-raw '{
//    "firstName": "Art",
//    "lastName": "Shyshkin",
//    "email": "myemail@example.com",
//    "password": "my super secret password with 1 number and A capital letter"
//}'

    @PostMapping(
            consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE},
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
    )
    @ResponseStatus(CREATED)
    public UserDto createUser(@Valid @RequestBody UserDetails userDetails) {
        UserDto userDto = UserDto.builder()
                .userId(UUID.randomUUID())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .build();
        userRepository.put(userDto.getUserId(), userDto);
        return userDto;
    }

    @PutMapping("{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") UUID userId, @Valid @RequestBody UserDto user) {
        if (!userRepository.containsKey(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with Id `" + userId + "` not found");
//        if (!userRepository.containsKey(userId)) return ResponseEntity.noContent().build();
        user.setUserId(userId);
        userRepository.put(userId, user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userRepository.remove(userId);
    }
}
