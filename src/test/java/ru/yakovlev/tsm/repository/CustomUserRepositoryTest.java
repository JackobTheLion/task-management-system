package ru.yakovlev.tsm.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yakovlev.tsm.dto.user.UserSearchCriteria;
import ru.yakovlev.tsm.model.user.User;
import ru.yakovlev.tsm.model.user.UserRole;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@ComponentScan
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Testcontainers
class CustomUserRepositoryTest {

    private final UserRepository userRepository;

    private final CustomUserRepository customUserRepository;

    @Container
    private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:14.3-alpine")
            .withDatabaseName("dbname")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

    User user1;

    User user2;

    @BeforeEach
    void beforeEach() {
        User user = User.builder()
                .email("email@gmail.ru")
                .userName("userName")
                .firstName("firstName")
                .lastName("lastName")
                .password("123")
                .userRole(List.of(new UserRole("ROLE_USER")))
                .enabled(true)
                .build();
        user1 = userRepository.save(user);

        user = User.builder()
                .email("otherEmail@yandex.ru")
                .userName("userName 2")
                .firstName("firstName")
                .lastName("lastName")
                .password("123")
                .userRole(List.of(new UserRole("ROLE_USER")))
                .enabled(true)
                .build();
        user2 = userRepository.save(user);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    public void findUsers_Normal_ByEmail() {
        UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder()
                .email(user2.getEmail())
                .from(0)
                .size(10)
                .build();

        List<User> users = customUserRepository.findUsers(userSearchCriteria);

        assertEquals(1, users.size());
        assertEquals(user2, users.get(0));
    }

    @Test
    public void findUsers_Normal_ByUserName() {
        UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder()
                .userName(user1.getUsername())
                .from(0)
                .size(10)
                .build();

        List<User> users = customUserRepository.findUsers(userSearchCriteria);

        assertEquals(1, users.size());
        assertEquals(user1, users.get(0));
    }

    @Test
    public void findUsers_Normal_FindAll() {
        UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder()
                .from(0)
                .size(10)
                .build();

        List<User> users = customUserRepository.findUsers(userSearchCriteria);

        assertEquals(2, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
    }
}