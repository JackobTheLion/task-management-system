package ru.yakovlev.tsm.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yakovlev.tsm.dto.user.UserDtoShortResponse;
import ru.yakovlev.tsm.model.task.Priority;
import ru.yakovlev.tsm.model.task.Status;

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
