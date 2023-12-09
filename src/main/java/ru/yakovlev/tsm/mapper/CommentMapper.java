package ru.yakovlev.tsm.mapper;

import ru.yakovlev.tsm.dto.task.CommentDtoRequest;
import ru.yakovlev.tsm.dto.task.CommentDtoResponse;
import ru.yakovlev.tsm.model.task.Comment;

public class CommentMapper {
    public static Comment mapFromDto(CommentDtoRequest commentDtoRequest) {
        return Comment.builder()
                .text(commentDtoRequest.getText())
                .build();
    }

    public static CommentDtoResponse mapToDto(Comment comment) {
        return CommentDtoResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.mapToShortDto(comment.getAuthor()))
                .build();
    }
}
