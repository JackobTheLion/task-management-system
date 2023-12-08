package ru.yakovlev.kanban.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDtoRequest {
    @NotBlank
    @Size(max = 500)
    private String text;
}
