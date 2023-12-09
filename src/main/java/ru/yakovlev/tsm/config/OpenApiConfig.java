package ru.yakovlev.tsm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Task Management System Api",
                description = "Simple backend for Task Management System APP.",
                contact = @Contact(
                        name = "Egor Yakovlev",
                        email = "riddik24@yandex.ru",
                        url = "https://github.com/JackobTheLion"
                )
        )
)
public class OpenApiConfig {
}