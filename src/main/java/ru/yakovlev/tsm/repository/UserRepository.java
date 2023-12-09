package ru.yakovlev.tsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yakovlev.tsm.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String name);
}
