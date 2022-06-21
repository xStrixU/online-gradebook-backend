package me.xstrixu.onlinegradebook.user;

import me.xstrixu.onlinegradebook.user.dto.UserDto;
import me.xstrixu.onlinegradebook.user.entity.User;

class UserFactory {

    UserDto userToUserDto(User user) {
        return new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
