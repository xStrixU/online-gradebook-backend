package me.xstrixu.onlinegradebook.user;

import me.xstrixu.onlinegradebook.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
