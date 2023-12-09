package ru.yakovlev.tsm.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yakovlev.tsm.dto.user.UserDtoFullResponse;
import ru.yakovlev.tsm.dto.user.UserDtoRequestUser;
import ru.yakovlev.tsm.dto.user.UserSearchCriteria;
import ru.yakovlev.tsm.exception.exceptions.EmailOrNameRegisteredException;
import ru.yakovlev.tsm.model.user.User;
import ru.yakovlev.tsm.model.user.UserRole;
import ru.yakovlev.tsm.repository.UserRepository;
import ru.yakovlev.tsm.service.UserService;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Testcontainers
public class UserServiceTest {
    private final UserService userService;
    private final UserRepository userRepository;

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


    private UserDtoRequestUser userDtoRequestUser;

    private UserDtoFullResponse expectedUser;

    private User mockUser;

    @BeforeEach
    public void beforeEach() {
        userDtoRequestUser = UserDtoRequestUser.builder()
                .email("email@ya.ru")
                .userName("userName")
                .password("123")
                .build();

        expectedUser = UserDtoFullResponse.builder()
                .id(2L)
                .email(userDtoRequestUser.getEmail())
                .userName(userDtoRequestUser.getUserName())
                .userRole(List.of(new UserRole(2L, "ROLE_USER")))
                .enabled(false)
                .build();

        mockUser = User.builder()
                .email(userDtoRequestUser.getEmail())
                .userName(userDtoRequestUser.getUserName())
                .enabled(expectedUser.getEnabled())
                .password("123")
                .build();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }


    @Test
    public void addUser_Normal() {
        UserDtoFullResponse actualUser = userService.addUser(userDtoRequestUser);

        assertEquals(expectedUser.getUserName(), actualUser.getUserName());
    }

    @Test
    public void addUser_LoginOrEmailAlreadyExists() {
        userRepository.save(mockUser);

        assertThrows(EmailOrNameRegisteredException.class, () -> userService.addUser(userDtoRequestUser));
    }

    @Test
    public void updateUser_Normal() {
        User save = userRepository.save(mockUser);
        String oldName = userDtoRequestUser.getUserName();
        userDtoRequestUser.setUserName("updatedName");
        expectedUser.setUserName(userDtoRequestUser.getUserName());
        expectedUser.setId(save.getId());
        Principal principal = () -> oldName;

        UserDtoFullResponse actualUser = userService.updateUser(userDtoRequestUser, principal);

        assertEquals(expectedUser.getUserName(), actualUser.getUserName());
    }

    @Test
    public void findUsers_findByUserName_Normal() {
        userRepository.save(mockUser);
        UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder()
                .userName(mockUser.getUsername())
                .from(0)
                .size(10)
                .build();
        User otherUser = User.builder()
                .email("email@gmail.com")
                .userName("other name")
                .enabled(true)
                .password("123")
                .build();
        userRepository.save(otherUser);

        List<UserDtoFullResponse> users = userService.findUsers(userSearchCriteria);

        assertEquals(1, users.size());
        assertEquals(expectedUser.getUserName(), users.get(0).getUserName());
    }

    @Test
    public void findUsers_findByEmailName_Normal() {
        userRepository.save(mockUser);
        UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder()
                .email(mockUser.getEmail())
                .from(0)
                .size(10)
                .build();
        User otherUser = User.builder()
                .email("email@gmail.com")
                .userName("other name")
                .enabled(true)
                .password("123")
                .build();
        userRepository.save(otherUser);

        List<UserDtoFullResponse> users = userService.findUsers(userSearchCriteria);

        assertEquals(1, users.size());
        assertEquals(expectedUser.getUserName(), users.get(0).getUserName());
    }

}
