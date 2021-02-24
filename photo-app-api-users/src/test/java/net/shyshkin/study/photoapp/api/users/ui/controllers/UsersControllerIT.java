package net.shyshkin.study.photoapp.api.users.ui.controllers;

import net.shyshkin.study.photoapp.api.users.data.UserRepository;
import net.shyshkin.study.photoapp.api.users.ui.model.CreateUserRequestModel;
import net.shyshkin.study.photoapp.api.users.ui.model.CreateUserResponseModel;
import net.shyshkin.study.photoapp.api.users.ui.model.LoginRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "eureka.client.register-with-eureka=false",
        "spring.cloud.discovery.enabled=false",
        "gateway.ip=127.0.0.1",

        "spring.jpa.properties.hibernate.generate_statistics=true",
        "logging.level.org.hibernate.stat=debug",
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.format_sql=true",
        "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
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

    @ParameterizedTest(name = "[{index}] {arguments}")
    @ValueSource(strings = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    void createUser(String mediaTypeString) {
        //given
        URI url = URI.create(UsersController.BASE_URL);
        CreateUserRequestModel userRequestModel = CreateUserRequestModel.builder()
                .firstName("Art")
                .lastName("Shyshkin")
                .email("super@example.com")
                .password("my super secret password with 1 number and A capital letter")
                .build();

        //when
        RequestEntity<CreateUserRequestModel> requestEntity = RequestEntity.post(url)
                .contentType(MediaType.valueOf(mediaTypeString))
                .accept(MediaType.valueOf(mediaTypeString))
                .body(userRequestModel);
        ResponseEntity<CreateUserResponseModel> responseEntity = restTemplate.exchange(requestEntity, CreateUserResponseModel.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.valueOf(mediaTypeString));
        CreateUserResponseModel userResponseModel = responseEntity.getBody();
        assertThat(userResponseModel).hasNoNullFieldsOrProperties()
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

    @Test
    @DisplayName("When absent in DB user tries to login should return UNAUTHORIZED status")
    void login_whenUserAbsent_mustBeUnauthorized() {
        //given
        URI url = URI.create("/login");
        LoginRequestModel loginRequestModel = LoginRequestModel.builder()
                .email("absent@example.com")
                .password("password of absent user")
                .build();

        //when
        RequestEntity<LoginRequestModel> requestEntity = RequestEntity.post(url)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(loginRequestModel);

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Map<String, Object> defaultErrorAttributesMap = responseEntity.getBody();
        assertThat(defaultErrorAttributesMap)
                .hasFieldOrProperty("timestamp")
                .hasFieldOrPropertyWithValue("status", 401)
                .hasFieldOrPropertyWithValue("error", "Unauthorized")
                .hasFieldOrProperty("message")
                .hasFieldOrPropertyWithValue("path", "/login");
    }

    @Test
    @DisplayName("When user present in DB tries to login with WRONG password should return UNAUTHORIZED status")
    void login_whenUserPresentButPasswordIsWrong_mustBeOk() {
        //given
        URI url = URI.create(UsersController.BASE_URL);
        CreateUserRequestModel userRequestModel = CreateUserRequestModel.builder()
                .firstName("Art")
                .lastName("Shyshkin")
                .email("super@example.com")
                .password("my super secret password with 1 number and A capital letter")
                .build();
        ResponseEntity<CreateUserResponseModel> responseEntityCreate = restTemplate.postForEntity(url, userRequestModel, CreateUserResponseModel.class);
        assertThat(responseEntityCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        url = URI.create("/login");
        LoginRequestModel loginRequestModel = LoginRequestModel.builder()
                .email("super@example.com")
                .password("WR0NG password")
                .build();

        //when
        RequestEntity<LoginRequestModel> requestEntity = RequestEntity.post(url)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(loginRequestModel);

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Map<String, Object> defaultErrorAttributesMap = responseEntity.getBody();
        assertThat(defaultErrorAttributesMap)
                .hasFieldOrProperty("timestamp")
                .hasFieldOrPropertyWithValue("status", 401)
                .hasFieldOrPropertyWithValue("error", "Unauthorized")
                .hasFieldOrProperty("message")
                .hasFieldOrPropertyWithValue("path", "/login");
    }

    @Test
    @DisplayName("When user present in DB tries to login should return OK status")
    void login_whenUserPresent_mustBeOk() {
        //given
        URI url = URI.create(UsersController.BASE_URL);
        CreateUserRequestModel userRequestModel = CreateUserRequestModel.builder()
                .firstName("Art")
                .lastName("Shyshkin")
                .email("super@example.com")
                .password("my super secret password with 1 number and A capital letter")
                .build();
        ResponseEntity<CreateUserResponseModel> responseEntityCreate = restTemplate.postForEntity(url, userRequestModel, CreateUserResponseModel.class);
        assertThat(responseEntityCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        url = URI.create("/login");
        LoginRequestModel loginRequestModel = LoginRequestModel.builder()
                .email("super@example.com")
                .password("my super secret password with 1 number and A capital letter")
                .build();

        //when
        RequestEntity<LoginRequestModel> requestEntity = RequestEntity.post(url)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(loginRequestModel);

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().get("token"))
                .isNotNull()
                .hasSize(1)
                .allSatisfy(System.out::println);
        assertThat(responseEntity.getHeaders().get("userId"))
                .isNotNull()
                .hasSize(1)
                .allSatisfy(System.out::println);
    }

}