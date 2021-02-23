package net.shyshkin.study.photoapp.api.users.services;

import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID());
        throw new RuntimeException("NOT IMPLEMENTED YET");
    }
}
