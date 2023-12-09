package ru.yakovlev.kanban.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yakovlev.kanban.dto.task.TaskSearchCriteria;
import ru.yakovlev.kanban.model.task.Priority;
import ru.yakovlev.kanban.model.task.Status;
import ru.yakovlev.kanban.model.task.Task;
import ru.yakovlev.kanban.model.user.User;
import ru.yakovlev.kanban.model.user.UserRole;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ComponentScan
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CustomTaskRepositoryTest {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CustomTaskRepository customTaskRepository;

    User author;

    User executor;

    Task task1;

    Task task2;

    @BeforeEach
    public void beforeEach() {
        User user = User.builder()
                .email("email@email.ru")
                .userName("userName")
                .firstName("firstName")
                .lastName("lastName")
                .password("123")
                .userRole(List.of(new UserRole("ROLE_USER")))
                .enabled(true)
                .build();
        author = userRepository.save(user);

        user = User.builder()
                .email("otherEmail@email.ru")
                .userName("userName 2")
                .firstName("firstName")
                .lastName("lastName")
                .password("123")
                .userRole(List.of(new UserRole("ROLE_USER")))
                .enabled(true)
                .build();
        executor = userRepository.save(user);

        Task taskToSave1 = Task.builder()
                .name("name 1")
                .description("description 1")
                .status(Status.IN_PROGRESS)
                .priority(Priority.MEDIUM)
                .author(author)
                .executor(author)
                .build();
        task1 = taskRepository.save(taskToSave1);

        Task taskToSave2 = Task.builder()
                .name("name 2")
                .description("description 2")
                .status(Status.IN_PROGRESS)
                .priority(Priority.MEDIUM)
                .author(author)
                .executor(executor)
                .build();
        task2 = taskRepository.save(taskToSave2);
    }

    @AfterEach
    public void afterEach() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void findTasks_Normal_DescriptionSearch() {
        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .description("desCRI")
                .from(0)
                .size(10)
                .build();

        List<Task> tasks = customTaskRepository.findTasks(taskSearchCriteria);

        assertEquals(2, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
    }

    @Test
    public void findTasks_Normal_Author() {
        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .author(author.getId())
                .from(0)
                .size(10)
                .build();

        List<Task> tasks = customTaskRepository.findTasks(taskSearchCriteria);

        assertEquals(2, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
    }

    @Test
    public void findTasks_Normal_Executor() {
        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .executor(executor.getId())
                .from(0)
                .size(10)
                .build();

        List<Task> tasks = customTaskRepository.findTasks(taskSearchCriteria);

        assertEquals(1, tasks.size());
        assertEquals(task2, tasks.get(0));
    }

    @Test
    public void findTasks_Normal_NoSuchTask() {
        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .description("NO SUCH DESCRIPTION")
                .from(0)
                .size(10)
                .build();

        List<Task> tasks = customTaskRepository.findTasks(taskSearchCriteria);

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void findTasks_Normal_LimitSize() {
        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .name("name")
                .from(0)
                .size(1)
                .build();

        List<Task> tasks = customTaskRepository.findTasks(taskSearchCriteria);

        assertEquals(1, tasks.size());
    }
}