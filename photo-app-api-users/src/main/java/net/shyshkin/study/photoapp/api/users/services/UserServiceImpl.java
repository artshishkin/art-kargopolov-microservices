package net.shyshkin.study.photoapp.api.users.services;

import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto createUser(UserDto userDetails) {
        throw new RuntimeException("NOT IMPLEMENTED YET");
    }
}
