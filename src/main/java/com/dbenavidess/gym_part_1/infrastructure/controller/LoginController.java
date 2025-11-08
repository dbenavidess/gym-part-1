package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.config.security.service.BruteForceSecurityService;
import com.dbenavidess.gym_part_1.config.security.service.JwtService;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.BruteForceSecurityEntity;
import com.dbenavidess.gym_part_1.infrastructure.response.LoginResponse;
import com.dbenavidess.gym_part_1.service.UserService;
import com.dbenavidess.gym_part_1.infrastructure.request.login.ChangePasswordRequest;
import com.dbenavidess.gym_part_1.infrastructure.request.login.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.NoSuchElementException;

@Tag(name = "Login", description = "User operations")
@RestController
public class LoginController {

    private final UserService userService;
    private final JwtService jwtService;
    private final BruteForceSecurityService bruteForceSecurityService;
    private final AuthenticationManager authenticationManager;

    public LoginController(UserService userService,
                           JwtService jwtService,
                           BruteForceSecurityService bruteForceSecurityService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.bruteForceSecurityService = bruteForceSecurityService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Login")
    @ApiResponse(responseCode = "200", description = "Login with username and password")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        BruteForceSecurityEntity bruteForceSecurity = bruteForceSecurityService.getByUsername(loginRequest.username);
        if (bruteForceSecurity == null)
            bruteForceSecurity = bruteForceSecurityService.createBruteForceSecurity(loginRequest.username);

        if(bruteForceSecurity.getUnlockDate() != null && bruteForceSecurity.getUnlockDate().after(new Date())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
        String jwt = userService.login(
                loginRequest.username,
                loginRequest.password,
                authenticationManager
        );
        bruteForceSecurityService.clearFailedAttempts(bruteForceSecurity);
            return new ResponseEntity<>(new LoginResponse(jwt), HttpStatus.OK);
        }catch (AuthenticationException e){
            User user = userService.getByUsername(loginRequest.username);
            if (user != null){
                bruteForceSecurityService.addFailedAttempt(user);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Logout")
    @ApiResponse(responseCode = "200", description = "Logout from account")
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            jwtService.deleteByUsername(jwtService.extractUsername(token));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
