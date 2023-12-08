package ru.yakovlev.kanban.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yakovlev.kanban.dto.user.UserDtoFullResponse;
import ru.yakovlev.kanban.dto.user.UserDtoRequest;
import ru.yakovlev.kanban.dto.user.UserSearchCriteria;
import ru.yakovlev.kanban.exception.exceptions.AccessDeniedException;
import ru.yakovlev.kanban.exception.exceptions.EmailOrNameRegisteredException;
import ru.yakovlev.kanban.exception.exceptions.NotFoundException;
import ru.yakovlev.kanban.mapper.UserMapper;
import ru.yakovlev.kanban.model.user.User;
import ru.yakovlev.kanban.model.user.UserRole;
import ru.yakovlev.kanban.repository.CustomUserRepository;
import ru.yakovlev.kanban.repository.UserRepository;
import ru.yakovlev.kanban.repository.UserRoleRepository;
import ru.yakovlev.kanban.service.UserService;

import java.security.Principal;
import java.util.List;

/**
 * Implementation of {@link UserService}
 */

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final CustomUserRepository customUserRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsService userDetailsService() {
        return this::findUserByName;
    }

    /**
     * Adding new user.
     *
     * @param userDtoRequest new user to add
     * @return {@link UserDtoFullResponse} saved user
     */
    @Override
    @Transactional
    public UserDtoFullResponse addUser(UserDtoRequest userDtoRequest) {
        log.trace("Saving user: {}.", userDtoRequest);
        List<UserRole> userRoles = userRoleRepository.findUserRoleByNameIn(userDtoRequest.getUserRole());

        if (userRoles.isEmpty()) {
            log.warn("User roles not found: {}", userDtoRequest.getUserRole());
            throw new NotFoundException(String.format("User roles not found: %s", userDtoRequest.getUserRole()));
        }

        log.trace("User roles found: {}", userRoles);
        User userToSave = UserMapper.mapFromDto(userDtoRequest, userRoles,
                passwordEncoder.encode(userDtoRequest.getPassword()));

        userToSave.setEnabled(false);

        User savedUser = saveUserToDb(userToSave);

        log.trace("User saved: {}", savedUser);
        return UserMapper.mapToFullDto(savedUser);
    }

    /**
     * Updating existing user. Non admin user can update only his own information.
     *
     * @param userDtoRequest user information to update
     * @param principal      current user
     * @return {@link UserDtoFullResponse} saved user
     */
    @Override
    @Transactional
    public UserDtoFullResponse updateUser(UserDtoRequest userDtoRequest, Principal principal) {
        log.trace("Updating user: {}.", userDtoRequest);

        if (!principal.getName().equals(userDtoRequest.getUserName())) {
            log.error("User '{}' cannot update user '{}'.", principal.getName(), userDtoRequest.getUserName());
            throw new AccessDeniedException(String.format("User '%s' cannot update user '%s'.",
                    principal.getName(), userDtoRequest.getUserName()));
        }

        User userToUpdate = findUserByName(userDtoRequest.getUserName());
        updateUserFields(userToUpdate, userDtoRequest);
        User updateduser = saveUserToDb(userToUpdate);
        log.trace("User updated: {}.", updateduser);
        return UserMapper.mapToFullDto(updateduser);
    }

    /**
     * User update by admin.
     *
     * @param userDtoRequest user information to update
     * @return {@link UserDtoFullResponse} saved user
     */
    @Override
    @Transactional
    public UserDtoFullResponse updateUserByAdmin(UserDtoRequest userDtoRequest) {
        log.trace("Updating user: {}.", userDtoRequest);
        User userToUpdate = findUserByName(userDtoRequest.getUserName());
        updateUserFields(userToUpdate, userDtoRequest);

        if (userDtoRequest.getUserRole().isEmpty()) {
            List<UserRole> userRoles = userRoleRepository.findUserRoleByNameIn(userDtoRequest.getUserRole());
            userToUpdate.setUserRole(userRoles);
        }

        User updateduser = userRepository.save(userToUpdate);
        log.trace("User updated: {}.", updateduser);
        return UserMapper.mapToFullDto(updateduser);
    }

    /**
     * Finding users based on criteria
     *
     * @param userSearchCriteria search criteria
     * @return {@link UserDtoFullResponse} list of users found
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDtoFullResponse> findUsers(UserSearchCriteria userSearchCriteria) {
        log.trace("Searching users: {}.", userSearchCriteria);
        List<User> users = customUserRepository.findUsers(userSearchCriteria);
        log.info("Number of users found: {}.", users.size());
        return users.stream().map(UserMapper::mapToFullDto).toList();
    }

    /**
     * Finding user by id
     *
     * @param id user id
     * @return {@link User}
     */
    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Finding user by name
     *
     * @param userName username
     * @return {@link User}
     */
    @Override
    @Transactional(readOnly = true)
    public User findUserByName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private void updateUserFields(User userToUpdate, UserDtoRequest userDtoRequest) {
        if (userDtoRequest.getEmail() != null) {
            userToUpdate.setEmail(userDtoRequest.getEmail());
        }

        if (userDtoRequest.getUserName() != null) {
            userToUpdate.setUserName(userDtoRequest.getUserName());
        }

        if (userDtoRequest.getFirstName() != null) {
            userToUpdate.setFirstName(userDtoRequest.getFirstName());
        }

        if (userDtoRequest.getLastName() != null) {
            userToUpdate.setLastName(userDtoRequest.getLastName());
        }

        if (userDtoRequest.getPassword() != null) {
            userToUpdate.setPassword(passwordEncoder.encode(userDtoRequest.getPassword()));
        }
    }

    private User saveUserToDb(User userToSave) {
        try {
            return userRepository.save(userToSave);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new EmailOrNameRegisteredException("User with such email or username already exists.");
        }
    }
}