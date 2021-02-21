package net.shyshkin.study.app.ws.services;

import net.shyshkin.study.app.ws.ui.model.UserDetails;
import net.shyshkin.study.app.ws.ui.model.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserDto> getAllUsers(int page, int limit, String sort);

    Optional<UserDto> getUser(UUID userId);

    UserDto createUser(UserDetails userDetails);

    UserDto updateUser(UserDto user);

    void deleteUser(UUID userId);
}
