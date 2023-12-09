package ru.yakovlev.kanban.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yakovlev.kanban.dto.task.TaskDtoFullResponse;
import ru.yakovlev.kanban.dto.task.TaskDtoRequest;
import ru.yakovlev.kanban.dto.task.TaskDtoShortResponse;
import ru.yakovlev.kanban.dto.task.TaskSearchCriteria;
import ru.yakovlev.kanban.dto.user.UserDtoFullResponse;
import ru.yakovlev.kanban.dto.user.UserDtoRequestAdmin;
import ru.yakovlev.kanban.dto.user.UserSearchCriteria;
import ru.yakovlev.kanban.model.task.Priority;
import ru.yakovlev.kanban.model.task.Status;
import ru.yakovlev.kanban.service.TaskService;
import ru.yakovlev.kanban.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Slf4j
@Validated
@Tag(name = "Admin", description = "Administrator endpoints")
@Schema(enumAsRef = true)
public class AdminController {

    private final UserService userService;

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "User search",
            description = "Searching users by various parameters")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDtoFullResponse> findUsers(@RequestParam(required = false) Long id,
                                               @RequestParam(required = false) String email,
                                               @RequestParam(required = false) String userName,
                                               @RequestParam(required = false) String firstName,
                                               @RequestParam(required = false) String lastName,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {

        UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder()
                .id(id)
                .email(email)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .from(from)
                .size(size).build();

        return userService.findUsers(userSearchCriteria);
    }

    @Operation(summary = "Task search",
            description = "Searching tasks by various parameters for admin. Includes 'deleted' parameter.")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDtoShortResponse> findTasks(@RequestParam(required = false) Long id,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) String description,
                                                @RequestParam(required = false) Status status,
                                                @RequestParam(required = false) Priority priority,
                                                @RequestParam(required = false) Long authorId,
                                                @RequestParam(required = false) Long executorId,
                                                @RequestParam(defaultValue = "false") Boolean deleted,
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
                .deleted(deleted)
                .from(from)
                .size(size)
                .build();

        return taskService.findTasks(taskSearchCriteria);
    }

    @Operation(summary = "Task update",
            description = "Task update by administrator")
    @PatchMapping("/{taskId}/update")
    @ResponseStatus(HttpStatus.OK)
    public TaskDtoFullResponse adminUpdateTask(@RequestBody TaskDtoRequest updatedTask, @PathVariable Long taskId) {
        return taskService.updateTaskAdmin(updatedTask, taskId);
    }

    @PatchMapping
    @Operation(summary = "Updating user information",
            description = "Updating user information by administrator")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoFullResponse adminUpdateUser(@RequestBody UserDtoRequestAdmin userDtoRequestAdmin) {
        return userService.updateUserByAdmin(userDtoRequestAdmin);
    }
}
