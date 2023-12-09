package ru.yakovlev.tsm.dto.task;

import lombok.Builder;
import lombok.Data;
import ru.yakovlev.tsm.dto.user.UserDtoShortResponse;

@Data
@Builder
public class CommentDtoResponse {
    private Long id;
    private String text;
    private UserDtoShortResponse author;
}
