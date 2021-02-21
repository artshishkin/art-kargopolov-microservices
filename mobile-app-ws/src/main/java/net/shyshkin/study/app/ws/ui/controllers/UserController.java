package net.shyshkin.study.app.ws.ui.controllers;

import net.shyshkin.study.app.ws.ui.model.UserDetails;
import net.shyshkin.study.app.ws.ui.model.dto.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
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
    public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
        UserDto userDto = UserDto.builder()
                .userId(userId)
                .firstName("Art")
                .lastName("Shyshkin")
                .email("myemail@example.com")
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ArtHeader", "SuperSecretArtHeaderValue");
        return new ResponseEntity<>(userDto, httpHeaders, 200);
    }

//curl --location --request POST 'http://localhost:8080/users' \
//--header 'Content-Type: application/json' \
//--header 'Cookie: JSESSIONID=DDE78460CC129994CDBE87744FE56B89' \
//--data-raw '{
//    "firstName": "Art",
//    "lastName": "Shyshkin",
//    "email": "myemail@example.com",
//    "password": "super_secret_password"
//}'

    @PostMapping(
            consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE},
            produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
    )
    @ResponseStatus(CREATED)
    public UserDto createUser(@RequestBody UserDetails userDetails) {
        return UserDto.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email("fake@example.com")
                .build();
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
