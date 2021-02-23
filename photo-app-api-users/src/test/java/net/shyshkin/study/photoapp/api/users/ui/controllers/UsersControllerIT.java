package net.shyshkin.study.photoapp.api.users.ui.controllers;

import net.shyshkin.study.photoapp.api.users.data.UserRepository;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import net.shyshkin.study.photoapp.api.users.ui.model.CreateUserRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "eureka.client.register-with-eureka=false",
        "spring.cloud.discovery.enabled=false"
})
class UsersControllerIT {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {
        //given
        URI url = URI.create(UsersController.BASE_URL);
        CreateUserRequestModel userRequestModel = CreateUserRequestModel.builder()
                .firstName("Art")
                .lastName("Shyshkin")
                .email("super@example.com")
                .password("my super secret password with 1 number and A capital letter")
                .build();

        //when
        ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity(url, userRequestModel, UserDto.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDto userDto = responseEntity.getBody();
        assertThat(userDto).hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("firstName", userRequestModel.getFirstName())
                .hasFieldOrPropertyWithValue("lastName", userRequestModel.getLastName())
                .hasFieldOrPropertyWithValue("email", userRequestModel.getEmail());
        assertThat(userRepository.count()).isEqualTo(1L);
        assertThat(userRepository.findAll())
                .allSatisfy(user ->
                        assertThat(user)
                                .hasNoNullFieldsOrProperties()
                                .hasFieldOrPropertyWithValue("firstName", userRequestModel.getFirstName())
                                .hasFieldOrPropertyWithValue("lastName", userRequestModel.getLastName())
                                .hasFieldOrPropertyWithValue("email", userRequestModel.getEmail()));
    }
}