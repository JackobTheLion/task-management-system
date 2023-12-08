package ru.yakovlev.kanban.model.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yakovlev.kanban.model.user.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tasks")
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executor_id")
    private User executor;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private List<Comment> comments;

    private Boolean deleted;

    @Override
    public String toString() {
        Long executorId = null;
        if (this.executor != null) {
            executorId = executor.getId();
        }

        Integer commentsSize = null;
        if (this.comments != null) {
            commentsSize = comments.size();
        }

        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", authorId=" + author.getId() +
                ", executorId=" + executorId +
                ", comments=" + commentsSize +
                ", deleted=" + deleted +
                '}';
    }
}
