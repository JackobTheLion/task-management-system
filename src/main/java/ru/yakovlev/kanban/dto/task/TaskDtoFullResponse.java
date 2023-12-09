package ru.yakovlev.kanban.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yakovlev.kanban.dto.user.UserDtoShortResponse;
import ru.yakovlev.kanban.model.task.Priority;
import ru.yakovlev.kanban.model.task.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDtoFullResponse {
    private Long id;

    private String name;

    private String description;

    private Status status;

    private Priority priority;

    private List<CommentDtoResponse> comments;

    private UserDtoShortResponse author;

    private UserDtoShortResponse executor;

    @Override
    public String toString() {
        return "TaskDtoFullResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description.substring(0, 50) + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", author=" + author +
                ", executor=" + executor +
                '}';
    }
}
