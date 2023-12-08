package ru.yakovlev.kanban.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yakovlev.kanban.model.task.Priority;
import ru.yakovlev.kanban.model.task.Status;
import ru.yakovlev.kanban.validation.ValidationGroups;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDtoRequest {
    @NotBlank(groups = ValidationGroups.Create.class)
    private String name;

    @NotBlank(groups = ValidationGroups.Create.class)
    private String description;

    private Status status;

    private Priority priority;

    private Long executor;

    private Boolean deleted;
}
