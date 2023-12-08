package ru.yakovlev.kanban.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Secure Password requirements\n" +
            "\n" +
            "Password must contain at least one digit [0-9].\n" +
            "Password must contain at least one lowercase Latin character [a-z].\n" +
            "Password must contain at least one uppercase Latin character [A-Z].\n" +
            "Password must contain at least one special character like ! @ # & ( ).\n" +
            "Password must contain a length of at least 8 characters and a maximum of 20 characters.";
}
