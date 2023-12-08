package ru.yakovlev.kanban.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yakovlev.kanban.dto.task.*;
import ru.yakovlev.kanban.model.task.Priority;
import ru.yakovlev.kanban.model.task.Status;
import ru.yakovlev.kanban.service.TaskService;
import ru.yakovlev.kanban.validation.ValidationGroups;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
@Slf4j
@Validated
@Tag(name = "Private task", description = "Private endpoint for tasks")
@Schema(enumAsRef = true)
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Adding new task",
            description = "Adding new task.")
    @PostMapping
    public TaskDtoFullResponse addTask(@RequestBody @Validated(ValidationGroups.Create.class) TaskDtoRequest taskDtoRequest,
                                       Principal principal) {
        return taskService.addTask(taskDtoRequest, principal);
    }

    @Operation(summary = "Updating task",
            description = "Only author of task can update it.")
    @PatchMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDtoFullResponse updateTask(@RequestBody @Validated(ValidationGroups.Update.class) TaskDtoRequest taskDtoRequest,
                                          Principal principal, @PathVariable Long taskId) {

        return taskService.updateTaskUser(taskDtoRequest, principal, taskId);
    }

    @Operation(summary = "Task search",
            description = "Searching tasks by various parameters")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDtoShortResponse> findTasks(@RequestParam(required = false) Long id,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) String description,
                                                @RequestParam(required = false) Status status,
                                                @RequestParam(required = false) Priority priority,
                                                @RequestParam(required = false) Long authorId,
                                                @RequestParam(required = false) Long executorId,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {

        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .id(id)
                .name(name)
                .description(description)
                .status(status)
                .priority(priority)
                .author(authorId)
                .executor(executorId)
                .from(from)
                .size(size)
                .build();

        return taskService.findTasks(taskSearchCriteria);
    }

    @Operation(summary = "Updating task status",
            description = "User can update status only for task, where he assigned as executor")
    @PatchMapping("/{taskId}/status")
    @ResponseStatus(HttpStatus.OK)
    public TaskDtoFullResponse updateStatus(@PathVariable @Min(1) Long taskId, @RequestParam Status status, Principal principal) {
        return taskService.updateStatus(taskId, status, principal);
    }

    @PostMapping("/{taskId}/comment")
    @Operation(summary = "Adding comment to task",
            description = "Adding comment to task")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse addComment(@PathVariable @Min(1) Long taskId,
                                         @RequestBody @Validated CommentDtoRequest commentDtoRequest,
                                         Principal principal) {

        return taskService.addComment(commentDtoRequest, principal, taskId);
    }
}
