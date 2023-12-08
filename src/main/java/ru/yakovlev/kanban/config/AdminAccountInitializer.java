package ru.yakovlev.kanban.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.yakovlev.kanban.model.user.User;
import ru.yakovlev.kanban.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminAccountInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.login:admin}")
    private String login;

    @Value("${admin.password:admin}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        User admin = User.builder()
                .userName(login)
                .email("no@email.com")
                .firstName("admin name")
                .lastName("admin name")
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build();
        userRepository.save(admin);

        log.error("--------------------------------------");
        log.error("Initial admin login '{}'.", login);
        log.error("Initial admin password '{}'.", password);
        log.error("--------------------------------------");
    }
}
