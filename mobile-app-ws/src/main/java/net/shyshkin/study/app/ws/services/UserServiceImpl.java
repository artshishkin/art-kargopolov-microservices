package net.shyshkin.study.app.ws.services;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.app.ws.shared.Utils;
import net.shyshkin.study.app.ws.ui.model.UserDetails;
import net.shyshkin.study.app.ws.ui.model.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Map<UUID, UserDto> userRepository = new HashMap<>();

    private final Utils utils;

    @Override
    public UserDto createUser(UserDetails userDetails) {
        UserDto userDto = UserDto.builder()
                .userId(utils.generateUserId())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .build();
        userRepository.put(userDto.getUserId(), userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto user) {
        UUID userId = user.getUserId();
        if (!userRepository.containsKey(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with Id `" + userId + "` not found");
        user.setUserId(userId);
        userRepository.put(userId, user);
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.remove(userId);
    }

    @Override
    public List<UserDto> getAllUsers(int page, int limit, String sort) {
        return userRepository.values()
                .stream()
                .skip((page - 1) * limit)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUser(UUID userId) {
        return Optional.ofNullable(userRepository.get(userId));
    }
}
