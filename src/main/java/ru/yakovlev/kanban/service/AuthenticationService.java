package ru.yakovlev.kanban.service;

import ru.yakovlev.kanban.dto.jwt.JwtRequest;
import ru.yakovlev.kanban.dto.jwt.JwtResponse;

/**
 * Service responsible for authentication/
 */
public interface AuthenticationService {

    /**
     * Accepts credentials returning JWT token
     *
     * @param jwtRequest user credentials
     * @return {@link JwtResponse} token
     */
    JwtResponse getToken(JwtRequest jwtRequest);
}
