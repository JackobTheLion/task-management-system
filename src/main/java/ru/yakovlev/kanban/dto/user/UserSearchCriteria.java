package ru.yakovlev.kanban.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchCriteria {
    private Long id;

    private String email;

    private String userName;

    private String firstName;

    private String lastName;

    private Boolean active;

    private Integer from;

    private Integer size;
}
