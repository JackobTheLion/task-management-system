package ru.yakovlev.kanban.mapper;

import ru.yakovlev.kanban.dto.user.UserDtoFullResponse;
import ru.yakovlev.kanban.dto.user.UserDtoRequest;
import ru.yakovlev.kanban.dto.user.UserDtoShortResponse;
import ru.yakovlev.kanban.model.user.User;
import ru.yakovlev.kanban.model.user.UserRole;

import java.util.List;

public class UserMapper {
    public static User mapFromDto(UserDtoRequest userDtoRequest, List<UserRole> roles, String password) {
        return User.builder()
                .userName(userDtoRequest.getUserName())
                .firstName(userDtoRequest.getFirstName())
                .lastName(userDtoRequest.getLastName())
                .email(userDtoRequest.getEmail())
                .password(password)
                .userRole(roles)
                .build();
    }

    public static UserDtoFullResponse mapToFullDto(User user) {
        return UserDtoFullResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userRole(user.getUserRole())
                .enabled(user.getEnabled())
                .build();
    }

    public static UserDtoShortResponse mapToShortDto(User user) {
        return UserDtoShortResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
