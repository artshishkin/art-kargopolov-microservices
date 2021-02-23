package net.shyshkin.study.photoapp.api.users.services;

import net.shyshkin.study.photoapp.api.users.shared.UserDto;

public interface UserService {

    UserDto createUser(UserDto userDetails);

}
