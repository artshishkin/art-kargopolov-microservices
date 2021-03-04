package net.shyshkin.study.photoapp.api.users.services;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.photoapp.api.users.data.UserRepository;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import net.shyshkin.study.photoapp.api.users.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@Profile("!rest-template")
public class UserServiceOpenFeignImpl extends AbstractUserService {

    private final AlbumsServiceClient albumsServiceClient;

    public UserServiceOpenFeignImpl(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    Supplier<ModelMapper> modelMapperFactory,
                                    AlbumsServiceClient albumsServiceClient) {
        super(userRepository, passwordEncoder, modelMapperFactory);
        this.albumsServiceClient = albumsServiceClient;
    }

    @Override
    public UserDto getUserDetailsByUserId(UUID userId) {
        ModelMapper mapper = modelMapperFactory.get();

        UserDto userDto = userRepository
                .findOneByUserId(userId)
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .orElseThrow(() -> new EntityNotFoundException("User with userId `" + userId + "` not found"));

//        try {
//            List<AlbumResponseModel> albums = albumsServiceClient.getUserAlbums(userId);
//            userDto.setAlbums(albums);
//        } catch (FeignException exception) {
//            log.error(exception.getLocalizedMessage());
//        }

        List<AlbumResponseModel> albums = albumsServiceClient.getUserAlbums(userId);
        userDto.setAlbums(albums);
        return userDto;
    }
}
