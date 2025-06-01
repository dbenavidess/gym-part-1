package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.application.service.UserService;
import com.dbenavidess.gym_part_1.infrastructure.request.Login.ChangePasswordRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.Login.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Login", description = "User operations")
@RestController
public class LoginController {

    UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Login")
    @ApiResponse(responseCode = "200", description = "Login with username and password")
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginRequest body){
        userService.login(body.username,body.password);
        return new ResponseEntity<>( HttpStatus.OK);
    }
    @Operation(summary = "Change password")
    @ApiResponse(responseCode = "200", description = "Change password using username and old password")
    @PutMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordRequest body){
        userService.changePassword(body.username,body.oldPassword,body.newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Change active")
    @ApiResponse(responseCode = "200", description = "Change current active status of user by username")
    @PatchMapping("/change-active")
    public ResponseEntity<Boolean> changeActive(@RequestBody String username){
        userService.changeActiveStatus(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
