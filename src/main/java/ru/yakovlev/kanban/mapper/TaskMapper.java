package ru.yakovlev.kanban.mapper;

import ru.yakovlev.kanban.dto.task.TaskDtoFullResponse;
import ru.yakovlev.kanban.dto.task.TaskDtoRequest;
import ru.yakovlev.kanban.dto.task.TaskDtoShortResponse;
import ru.yakovlev.kanban.model.task.Task;

public class TaskMapper {
    public static Task mapFromDto(TaskDtoRequest taskDtoRequest) {
        return Task.builder()
                .name(taskDtoRequest.getName())
                .description(taskDtoRequest.getDescription())
                .status(taskDtoRequest.getStatus())
                .priority(taskDtoRequest.getPriority())
                .build();
    }

    public static TaskDtoFullResponse mapToFullDto(Task task) {
        return TaskDtoFullResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(UserMapper.mapToShortDto(task.getAuthor()))
                .executor(UserMapper.mapToShortDto(task.getExecutor()))
                .build();
    }

    public static TaskDtoShortResponse mapToShortDto(Task task) {
        return TaskDtoShortResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .status(task.getStatus())
                .priority(task.getPriority())
                .executor(UserMapper.mapToShortDto(task.getExecutor()))
                .build();
    }
}
