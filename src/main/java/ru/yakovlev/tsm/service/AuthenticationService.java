package ru.yakovlev.tsm.service;

import ru.yakovlev.tsm.dto.jwt.JwtRequest;
import ru.yakovlev.tsm.dto.jwt.JwtResponse;

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
