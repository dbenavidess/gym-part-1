package com.dbenavidess.gym_part_1.infrastructure.response;


import org.springframework.hateoas.RepresentationModel;

public class SignupResponse extends RepresentationModel<SignupResponse> {
    public String username;
    public String password;

    public SignupResponse() {
    }

    public SignupResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
