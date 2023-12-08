package ru.yakovlev.kanban.dto.user;

import lombok.Builder;
import lombok.Data;
import ru.yakovlev.kanban.model.user.UserRole;

import java.util.List;

@Data
@Builder
public class UserSearchCriteria {
    private Long id;

    private String email;

    private String userName;

    private String firstName;

    private String lastName;

    private List<UserRole> userRole;

    private Boolean active;

    private Integer from;

    private Integer size;
}
