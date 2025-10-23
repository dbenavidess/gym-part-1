package com.dbenavidess.gym_part_1.infrastructure.request.login;

import jakarta.validation.constraints.NotNull;

public class LoginRequest {
    @NotNull
    public String username;
    @NotNull
    public String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
