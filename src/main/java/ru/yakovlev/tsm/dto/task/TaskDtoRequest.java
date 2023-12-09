package ru.yakovlev.tsm.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yakovlev.tsm.model.task.Priority;
import ru.yakovlev.tsm.model.task.Status;
import ru.yakovlev.tsm.validation.ValidationGroups;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDtoRequest {
    @NotBlank(groups = ValidationGroups.Create.class)
    private String name;

    @NotBlank(groups = ValidationGroups.Create.class)
    private String description;

    @Builder.Default
    private Status status = Status.IN_PROGRESS;

    @Builder.Default
    private Priority priority = Priority.MEDIUM;

    private Long executor;

    private Boolean deleted;
}
