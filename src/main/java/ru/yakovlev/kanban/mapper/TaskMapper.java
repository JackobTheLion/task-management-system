package ru.yakovlev.kanban.mapper;

import ru.yakovlev.kanban.dto.task.CommentDtoResponse;
import ru.yakovlev.kanban.dto.task.TaskDtoFullResponse;
import ru.yakovlev.kanban.dto.task.TaskDtoRequest;
import ru.yakovlev.kanban.dto.task.TaskDtoShortResponse;
import ru.yakovlev.kanban.dto.user.UserDtoShortResponse;
import ru.yakovlev.kanban.model.task.Task;

import java.util.ArrayList;
import java.util.List;

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
        UserDtoShortResponse executor = null;
        if (task.getExecutor() != null) {
            executor = UserMapper.mapToShortDto(task.getExecutor());
        }

        List<CommentDtoResponse> comments = new ArrayList<>();
        if (task.getComments() != null && !task.getComments().isEmpty()) {
            comments = task.getComments().stream().map(CommentMapper::mapToDto).toList();
        }

        return TaskDtoFullResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(UserMapper.mapToShortDto(task.getAuthor()))
                .comments(comments)
                .executor(executor)
                .build();
    }

    public static TaskDtoShortResponse mapToShortDto(Task task) {
        UserDtoShortResponse executor = null;
        if (task.getExecutor() != null) {
            executor = UserMapper.mapToShortDto(task.getExecutor());
        }

        return TaskDtoShortResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .status(task.getStatus())
                .priority(task.getPriority())
                .executor(executor)
                .build();
    }
}
