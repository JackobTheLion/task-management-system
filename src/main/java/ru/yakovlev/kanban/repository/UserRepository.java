package ru.yakovlev.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yakovlev.kanban.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String name);
}
