package ru.yakovlev.kanban.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yakovlev.kanban.validation.Password;
import ru.yakovlev.kanban.validation.ValidationGroups;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Create and update user request")
public class UserDtoRequest {

    @Email(groups = ValidationGroups.Create.class)
    @NotBlank
    private String email;

    @NotEmpty(groups = ValidationGroups.Create.class)
    private String userName;

    @NotEmpty(groups = ValidationGroups.Create.class)
    private String firstName;

    @NotEmpty(groups = ValidationGroups.Create.class)
    private String lastName;

    @NotEmpty
    @Password
    private String password;

    @Schema(description = "'ROLE_ADMIN', 'ROLE_USER'")
    @NotNull(groups = ValidationGroups.Create.class)
    private List<String> userRole;

    @Override
    public String toString() {
        return "UserDtoRequest{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}