package ru.yakovlev.kanban.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ru.yakovlev.kanban.exception.exceptions.AccessDeniedException;

import java.io.IOException;

@Component
public class AuthExceptionHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        throw new AccessDeniedException("Wrong username or password");
    }
}
