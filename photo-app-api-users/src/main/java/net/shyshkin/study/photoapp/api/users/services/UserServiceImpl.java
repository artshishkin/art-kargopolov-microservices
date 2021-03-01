package net.shyshkin.study.photoapp.api.users.services;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.photoapp.api.users.data.UserEntity;
import net.shyshkin.study.photoapp.api.users.data.UserRepository;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import net.shyshkin.study.photoapp.api.users.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpMethod.GET;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final ParameterizedTypeReference<List<AlbumResponseModel>> ALBUM_LIST_TYPE_REFERENCE = new ParameterizedTypeReference<>() {
    };

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final Environment environment;

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID());
        userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity user = mapper.map(userDetails, UserEntity.class);
        userRepository.save(user);
        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.setPassword("***");
        return userDto;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return userRepository
                .findOneByEmail(email)
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .orElseThrow(() -> new UsernameNotFoundException("User: `" + email + "` not found"));
    }

    @Override
    public UserDto getUserDetailsByUserId(UUID userId) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = userRepository
                .findOneByUserId(userId)
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .orElseThrow(() -> new EntityNotFoundException("User with userId `" + userId + "` not found"));

        String albumsUrl = environment.getRequiredProperty("albums.url");
//        String albumsUrl = "http://ALBUMS-WS/users/{userId}/albums";
        ResponseEntity<List<AlbumResponseModel>> responseEntity = restTemplate.exchange(albumsUrl, GET,
                null, ALBUM_LIST_TYPE_REFERENCE, userId);
        List<AlbumResponseModel> albums = responseEntity.getBody();
        userDto.setAlbums(albums);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findOneByEmail(username)
                .map(userEntity -> new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("User: `" + username + "` not found"));
    }
}
