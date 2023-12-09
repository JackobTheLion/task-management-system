package ru.yakovlev.kanban.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import ru.yakovlev.kanban.dto.task.*;
import ru.yakovlev.kanban.exception.exceptions.AccessDeniedException;
import ru.yakovlev.kanban.model.task.Priority;
import ru.yakovlev.kanban.model.task.Status;
import ru.yakovlev.kanban.model.user.User;
import ru.yakovlev.kanban.repository.CommentRepository;
import ru.yakovlev.kanban.repository.TaskRepository;
import ru.yakovlev.kanban.repository.UserRepository;
import ru.yakovlev.kanban.service.TaskService;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Rollback
class TaskServiceImplIT {
    private final TaskService taskService;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    User author;
    User executor;

    TaskDtoRequest taskToSave;

    TaskDtoFullResponse expectedTask;

    Principal principal;

    @BeforeEach
    public void beforeEach() {
        author = User.builder()
                .email("email@ya.ru")
                .userName("userName")
                .password("123")
                .build();

        executor = User.builder()
                .email("email@gmail.ru")
                .userName("otherUserName")
                .password("123")
                .build();

        taskToSave = TaskDtoRequest.builder()
                .name("task name")
                .description("task description")
                .status(Status.DONE)
                .priority(Priority.MEDIUM)
                .build();

        expectedTask = TaskDtoFullResponse.builder()
                .name(taskToSave.getName())
                .description(taskToSave.getDescription())
                .status(taskToSave.getStatus())
                .priority(taskToSave.getPriority())
                .build();

        principal = () -> author.getUsername();
    }

    @AfterEach
    public void afterEach(){
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void addTaskTest_Normal() {
        userRepository.save(author);

        TaskDtoFullResponse actualTask = taskService.addTask(taskToSave, principal);

        assertEquals(expectedTask.getName(), actualTask.getName());
    }

    @Test
    public void updateTask_Normal() {
        userRepository.save(author);
        TaskDtoFullResponse savedTask = taskService.addTask(taskToSave, principal);
        taskToSave.setName("new name");

        TaskDtoFullResponse actualTask = taskService.updateTaskUser(taskToSave, principal, savedTask.getId());

        assertEquals(taskToSave.getName(), actualTask.getName());
    }

    @Test
    public void updateStatus_Normal() {
        userRepository.save(author);
        Long id = userRepository.save(executor).getId();
        taskToSave.setExecutor(id);
        TaskDtoFullResponse savedTask = taskService.addTask(taskToSave, principal);
        Principal executorPrincipal = () -> executor.getUsername();

        TaskDtoFullResponse updatedTask = taskService.updateStatus(savedTask.getId(), Status.IN_PROGRESS, executorPrincipal);

        assertEquals(expectedTask.getName(), updatedTask.getName());
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    public void updateStatus_NoRights() {
        userRepository.save(author);
        Long id = userRepository.save(executor).getId();
        taskToSave.setExecutor(id);
        TaskDtoFullResponse savedTask = taskService.addTask(taskToSave, principal);
        Principal executorPrincipal = () -> "RANDOM NAME";

        assertThrows(AccessDeniedException.class, () ->
                taskService.updateStatus(savedTask.getId(), Status.IN_PROGRESS, executorPrincipal));
    }


    @Test
    public void findTasks_Normal() {
        userRepository.save(author);
        taskService.addTask(taskToSave, principal);

        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .name(taskToSave.getName())
                .from(0)
                .size(10)
                .build();

        List<TaskDtoShortResponse> tasks = taskService.findTasks(taskSearchCriteria);

        assertEquals(1, tasks.size());
        assertEquals(taskToSave.getName(), tasks.get(0).getName());
    }

    @Test
    public void addComment() {
        User savedUser = userRepository.save(author);
        TaskDtoFullResponse savedTask = taskService.addTask(taskToSave, principal);
        CommentDtoRequest commentDtoRequest = CommentDtoRequest.builder()
                .text("This is a comment")
                .build();

        CommentDtoResponse commentDtoResponse = taskService.addComment(commentDtoRequest, principal, savedTask.getId());

        assertEquals(commentDtoRequest.getText(), commentDtoResponse.getText());
        assertEquals(savedUser.getId(), commentDtoResponse.getAuthor().getId());

        List<CommentDtoResponse> comments = taskService.findTaskById(savedTask.getId()).getComments();

        assertEquals(1, comments.size());
        assertEquals(commentDtoRequest.getText(), comments.get(0).getText());
    }

}