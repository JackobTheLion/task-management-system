package ru.yakovlev.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yakovlev.kanban.model.task.Task;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByIdAndAuthorUserName(Long taskId, String authorUserName);
}
