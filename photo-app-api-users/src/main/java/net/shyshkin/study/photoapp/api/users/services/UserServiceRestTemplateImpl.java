package net.shyshkin.study.photoapp.api.users.services;

import net.shyshkin.study.photoapp.api.users.data.UserRepository;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import net.shyshkin.study.photoapp.api.users.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static org.springframework.http.HttpMethod.GET;

@Service
@Profile("rest-template")
public class UserServiceRestTemplateImpl extends AbstractUserService {

    private static final ParameterizedTypeReference<List<AlbumResponseModel>> ALBUM_LIST_TYPE_REFERENCE = new ParameterizedTypeReference<>() {
    };

    private final RestTemplate restTemplate;
    private final Environment environment;

    public UserServiceRestTemplateImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, Supplier<ModelMapper> modelMapperFactory, RestTemplate restTemplate, Environment environment) {
        super(userRepository, passwordEncoder, modelMapperFactory);
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

//    public UserServiceRestTemplateImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate, Environment environment) {
//        super(userRepository, passwordEncoder);
//        this.restTemplate = restTemplate;
//        this.environment = environment;
//    }

    @Override
    public UserDto getUserDetailsByUserId(UUID userId) {
        ModelMapper mapper = modelMapperFactory.get();

        UserDto userDto = userRepository
                .findOneByUserId(userId)
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .orElseThrow(() -> new EntityNotFoundException("User with userId `" + userId + "` not found"));

        String albumsUrl = environment.getRequiredProperty("albums.url");
        ResponseEntity<List<AlbumResponseModel>> responseEntity = restTemplate.exchange(albumsUrl, GET,
                null, ALBUM_LIST_TYPE_REFERENCE, userId);
        List<AlbumResponseModel> albums = responseEntity.getBody();
        userDto.setAlbums(albums);
        return userDto;
    }
}
