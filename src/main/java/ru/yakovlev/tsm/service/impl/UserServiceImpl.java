package ru.yakovlev.tsm.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yakovlev.tsm.dto.user.UserDtoFullResponse;
import ru.yakovlev.tsm.dto.user.UserDtoRequestAdmin;
import ru.yakovlev.tsm.dto.user.UserDtoRequestUser;
import ru.yakovlev.tsm.dto.user.UserSearchCriteria;
import ru.yakovlev.tsm.exception.exceptions.EmailOrNameRegisteredException;
import ru.yakovlev.tsm.exception.exceptions.NotFoundException;
import ru.yakovlev.tsm.mapper.UserMapper;
import ru.yakovlev.tsm.model.user.User;
import ru.yakovlev.tsm.model.user.UserRole;
import ru.yakovlev.tsm.repository.CustomUserRepository;
import ru.yakovlev.tsm.repository.UserRepository;
import ru.yakovlev.tsm.repository.UserRoleRepository;
import ru.yakovlev.tsm.service.UserService;

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
     * @param userDtoRequestUser new user to add
     * @return {@link UserDtoFullResponse} saved user
     */
    @Override
    @Transactional
    public UserDtoFullResponse addUser(UserDtoRequestUser userDtoRequestUser) {
        log.trace("Saving user: {}.", userDtoRequestUser);

        User userToSave = UserMapper.mapFromDto(userDtoRequestUser, passwordEncoder.encode(userDtoRequestUser.getPassword()));
        UserRole userRole = userRoleRepository.findUserRoleByName("ROLE_USER");
        userToSave.setUserRole(List.of(userRole));
        userToSave.setEnabled(false);

        User savedUser = saveUserToDb(userToSave);

        log.trace("User saved: {}", savedUser);
        return UserMapper.mapToFullDto(savedUser);
    }

    /**
     * Updating existing user. Non admin user can update only his own information.
     *
     * @param userDtoRequestUser user information to update
     * @param principal          current user
     * @return {@link UserDtoFullResponse} saved user
     */
    @Override
    @Transactional
    public UserDtoFullResponse updateUser(UserDtoRequestUser userDtoRequestUser, Principal principal) {
        log.trace("Updating user: {}.", userDtoRequestUser);
        User userToUpdate = findUserByName(principal.getName());
        updateUserFields(userToUpdate, userDtoRequestUser);
        User updateduser = saveUserToDb(userToUpdate);
        log.trace("User updated: {}.", updateduser);
        return UserMapper.mapToFullDto(updateduser);
    }

    /**
     * User update by admin.
     *
     * @param userDtoRequestUser user information to update
     * @return {@link UserDtoFullResponse} saved user
     */
    @Override
    @Transactional
    public UserDtoFullResponse updateUserByAdmin(UserDtoRequestAdmin userDtoRequestUser, Long userId) {
        log.trace("Updating user: {}.", userDtoRequestUser);
        User userToUpdate = findUserById(userId);
        updateUserFields(userToUpdate, userDtoRequestUser);

        if (userDtoRequestUser.getUserRole() != null && !userDtoRequestUser.getUserRole().isEmpty()) {
            List<UserRole> userRoles = userRoleRepository.findUserRoleByNameIn(userDtoRequestUser.getUserRole());
            userToUpdate.setUserRole(userRoles);
        }

        if (userDtoRequestUser.getEnabled() != null) {
            userToUpdate.setEnabled(userDtoRequestUser.getEnabled());
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
                .orElseThrow(() -> new NotFoundException("User not found"));
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
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void updateUserFields(User userToUpdate, UserDtoRequestUser userDtoRequestUser) {
        if (userDtoRequestUser.getEmail() != null) {
            userToUpdate.setEmail(userDtoRequestUser.getEmail());
        }

        if (userDtoRequestUser.getUserName() != null) {
            userToUpdate.setUserName(userDtoRequestUser.getUserName());
        }

        if (userDtoRequestUser.getFirstName() != null) {
            userToUpdate.setFirstName(userDtoRequestUser.getFirstName());
        }

        if (userDtoRequestUser.getLastName() != null) {
            userToUpdate.setLastName(userDtoRequestUser.getLastName());
        }

        if (userDtoRequestUser.getPassword() != null) {
            userToUpdate.setPassword(passwordEncoder.encode(userDtoRequestUser.getPassword()));
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