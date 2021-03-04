package net.shyshkin.study.photoapp.api.users.services;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.photoapp.api.users.data.UserEntity;
import net.shyshkin.study.photoapp.api.users.data.UserRepository;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
abstract class AbstractUserService implements UserService {

    protected final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    protected final Supplier<ModelMapper> modelMapperFactory;

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID());
        userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));

        ModelMapper mapper = modelMapperFactory.get();
        UserEntity user = mapper.map(userDetails, UserEntity.class);
        userRepository.save(user);
        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.setPassword("***");
        return userDto;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        ModelMapper mapper = modelMapperFactory.get();
        return userRepository
                .findOneByEmail(email)
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .orElseThrow(() -> new UsernameNotFoundException("User: `" + email + "` not found"));
    }

    @Override
    abstract public UserDto getUserDetailsByUserId(UUID userId);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findOneByEmail(username)
                .map(userEntity -> new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("User: `" + username + "` not found"));
    }
}
