package ru.yakovlev.kanban.dto.task;

import lombok.Builder;
import lombok.Data;
import ru.yakovlev.kanban.model.task.Priority;
import ru.yakovlev.kanban.model.task.Status;

@Data
@Builder
public class TaskSearchCriteria {
    private Long id;

    private String name;

    private String description;

    private Status status;

    private Priority priority;

    private Long author;

    private Long executor;

    private Integer from;

    private Integer size;

    private Boolean deleted;
}
