package ru.yakovlev.tsm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yakovlev.tsm.validation.password.Password;

import static ru.yakovlev.tsm.validation.ValidationGroups.Create;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Create and update user request")
public class UserDtoRequestUser {

    @Email(groups = Create.class)
    @NotBlank
    private String email;

    @NotEmpty(groups = Create.class)
    private String userName;

    private String firstName;

    private String lastName;

    @NotEmpty(groups = Create.class)
    @Password
    private String password;

    @Override
    public String toString() {
        return "UserDtoRequest{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}