package com.dbenavidess.gym_part_1.infrastructure.response;


import org.springframework.hateoas.RepresentationModel;

public class SignupResponse extends RepresentationModel<SignupResponse> {
    public String username;
    public String password;
    public String token;

    public SignupResponse() {
    }

    public SignupResponse(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

}
