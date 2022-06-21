package me.xstrixu.onlinegradebook.user;

import me.xstrixu.onlinegradebook.exception.ResourceNotFoundException;
import me.xstrixu.onlinegradebook.user.dto.UserDto;
import me.xstrixu.onlinegradebook.user.entity.User;

record UserService(UserRepository userRepository, UserFactory userFactory) {

    UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found!"));

        return userFactory.userToUserDto(user);
    }
}
