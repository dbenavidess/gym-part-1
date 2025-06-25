package com.dbenavidess.gym_part_1.infrastructure.response;

public class LoginResponse {
    public String jwtToken;

    public LoginResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
