package ru.yakovlev.kanban.mapper;

import ru.yakovlev.kanban.dto.task.CommentDtoRequest;
import ru.yakovlev.kanban.dto.task.CommentDtoResponse;
import ru.yakovlev.kanban.model.task.Comment;

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
