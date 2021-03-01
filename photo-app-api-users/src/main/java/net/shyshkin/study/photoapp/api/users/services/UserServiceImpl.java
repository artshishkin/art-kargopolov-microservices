package net.shyshkin.study.photoapp.api.users.services;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.photoapp.api.users.data.UserEntity;
import net.shyshkin.study.photoapp.api.users.data.UserRepository;
import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        return userRepository
                .findOneByUserId(userId)
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .orElseThrow(() -> new EntityNotFoundException("User with userId `" + userId + "` not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findOneByEmail(username)
                .map(userEntity -> new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("User: `" + username + "` not found"));
    }
}
