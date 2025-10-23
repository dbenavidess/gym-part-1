package com.dbenavidess.gym_part_1.infrastructure.request.login;

import jakarta.validation.constraints.NotNull;

public class ChangePasswordRequest {
    @NotNull
    public String username;
    @NotNull
    public String oldPassword;
    @NotNull
    public String newPassword;

    public ChangePasswordRequest(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
