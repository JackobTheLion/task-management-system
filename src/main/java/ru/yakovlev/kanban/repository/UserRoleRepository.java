package ru.yakovlev.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yakovlev.kanban.model.user.UserRole;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findUserRoleByNameIn(List<String> role);
}
