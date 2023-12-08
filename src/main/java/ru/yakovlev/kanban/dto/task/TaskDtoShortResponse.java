package ru.yakovlev.kanban.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yakovlev.kanban.dto.user.UserDtoShortResponse;
import ru.yakovlev.kanban.model.task.Priority;
import ru.yakovlev.kanban.model.task.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDtoShortResponse {
    private Long id;

    private String name;

    private Status status;

    private Priority priority;

    private UserDtoShortResponse executor;
}
