package net.shyshkin.study.photoapp.api.users.services;

import net.shyshkin.study.photoapp.api.users.data.UserRepository;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import net.shyshkin.study.photoapp.api.users.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@Profile("!rest-template")
public class UserServiceOpenFeignImpl extends AbstractUserService {

    private final AlbumsServiceClient albumsServiceClient;

    public UserServiceOpenFeignImpl(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    AlbumsServiceClient albumsServiceClient) {
        super(userRepository, passwordEncoder);
        this.albumsServiceClient = albumsServiceClient;
    }

    @Override
    public UserDto getUserDetailsByUserId(UUID userId) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = userRepository
                .findOneByUserId(userId)
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .orElseThrow(() -> new EntityNotFoundException("User with userId `" + userId + "` not found"));

        List<AlbumResponseModel> albums = albumsServiceClient.getUserAlbums(userId);
        userDto.setAlbums(albums);
        return userDto;
    }
}
