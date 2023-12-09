package ru.yakovlev.tsm.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.yakovlev.tsm.dto.jwt.JwtRequest;
import ru.yakovlev.tsm.dto.jwt.JwtResponse;
import ru.yakovlev.tsm.service.AuthenticationService;
import ru.yakovlev.tsm.service.JwtService;
import ru.yakovlev.tsm.service.UserService;

/**
 * Implementation of {@link AuthenticationService}
 */

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    /**
     * Accepts credentials returning JWT token
     *
     * @param jwtRequest user credentials
     * @return {@link JwtResponse} token
     */
    @Override
    public JwtResponse getToken(JwtRequest jwtRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword()));
        UserDetails userDetails = userService.findUserByName(jwtRequest.getUserName());
        return new JwtResponse(jwtService.generateToken(userDetails));
    }
}
