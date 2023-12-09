package ru.yakovlev.tsm.dto.jwt;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
