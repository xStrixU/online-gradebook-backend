package me.xstrixu.onlinegradebook.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

    @Bean
    UserService userService(UserRepository userRepository) {
        return new UserService(userRepository, new UserFactory());
    }
}
