package ru.yakovlev.kanban.dto.jwt;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JwtRequest {
    @NotEmpty
    private String userName;

    @NotEmpty
    private String password;

    @Override
    public String toString() {
        return "JwtRequest{" +
                "userName='" + userName + '\'' +
                ", password='{masked}'" +
                '}';
    }
}
