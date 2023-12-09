package ru.yakovlev.tsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yakovlev.tsm.model.task.Task;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByIdAndAuthorUserName(Long taskId, String authorUserName);
}
