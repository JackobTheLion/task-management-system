package ru.yakovlev.tsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yakovlev.tsm.model.task.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
