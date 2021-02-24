package net.shyshkin.study.photoapp.api.users.services;

import net.shyshkin.study.photoapp.api.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDetails);

}
