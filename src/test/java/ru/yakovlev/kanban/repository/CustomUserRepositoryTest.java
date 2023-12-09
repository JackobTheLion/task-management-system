package ru.yakovlev.kanban.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import ru.yakovlev.kanban.dto.user.UserSearchCriteria;
import ru.yakovlev.kanban.model.user.User;
import ru.yakovlev.kanban.model.user.UserRole;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CustomUserRepositoryTest {
    private final UserRepository userRepository;

    private final CustomUserRepository customUserRepository;

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