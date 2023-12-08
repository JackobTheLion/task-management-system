package ru.yakovlev.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yakovlev.kanban.model.task.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
