package ru.yakovlev.kanban.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.yakovlev.kanban.dto.user.UserDtoFullResponse;
import ru.yakovlev.kanban.dto.user.UserDtoRequestAdmin;
import ru.yakovlev.kanban.dto.user.UserDtoRequestUser;
import ru.yakovlev.kanban.dto.user.UserSearchCriteria;
import ru.yakovlev.kanban.model.user.User;

import java.security.Principal;
import java.util.List;

/**
 * Service for working with users.
 */
public interface UserService {

    UserDetailsService userDetailsService();

    /**
     * Adding new user.
     *
     * @param userDtoRequestUser new user to add
     * @return {@link UserDtoFullResponse} saved user
     */
    UserDtoFullResponse addUser(UserDtoRequestUser userDtoRequestUser);

    /**
     * Updating existing user
     *
     * @param userDtoRequestUser user information to update
     * @param principal          current user
     * @return {@link UserDtoFullResponse} saved user
     */
    UserDtoFullResponse updateUser(UserDtoRequestUser userDtoRequestUser, Principal principal);

    /**
     * User update by admin.
     *
     * @param userDtoRequestUser user information to update
     * @return {@link UserDtoFullResponse} saved user
     */
    UserDtoFullResponse updateUserByAdmin(UserDtoRequestAdmin userDtoRequestUser);

    /**
     * Finding user by id
     *
     * @param id user id
     * @return {@link User}
     */
    User findUserById(Long id);

    /**
     * Finding user by name
     *
     * @param userName username
     * @return {@link User}
     */
    User findUserByName(String userName);

    /**
     * Finding users based on criteria
     *
     * @param userSearchCriteria search criteria
     * @return {@link UserDtoFullResponse} list of users found
     */
    List<UserDtoFullResponse> findUsers(UserSearchCriteria userSearchCriteria);

}
