package com.dbenavidess.gym_part_1.service;

import com.dbenavidess.gym_part_1.config.security.service.JwtService;
import com.dbenavidess.gym_part_1.domain.model.User;
import com.dbenavidess.gym_part_1.domain.repository.UserRepository;
import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncryptionProvider passwordEncryptionProvider;
    private final JwtService jwtService;

    public UserService(UserRepository repository, PasswordEncryptionProvider passwordEncryptionProvider,  JwtService jwtService) {
        this.repository = repository;
        this.passwordEncryptionProvider = passwordEncryptionProvider;
        this.jwtService = jwtService;
    }


    public String login(String username, String password,AuthenticationManager authenticationManager){

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    username,
                    password
            )
    );
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    jwtService.deleteByUsername(username);
    return jwtService.generateToken(new HashMap<>(), userDetails);
    }

    public boolean loginMatch(String username, String password){
        User user = repository.searchUsername(username);

        return user != null && passwordEncryptionProvider.matches(password,user.getPassword());
    }

    public void changeActiveStatus(String username){
        User user = repository.getUserByUsername(username);
        User updatedUser = new User(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword(),
                !user.getIsActive()
                );
        repository.updateUser(updatedUser);
    }

    public void changePassword(String username, String oldPassword, String newPassword){

        User user = repository.getUserByUsername(username);
        User updatedUser = repository.updateUser(new User(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                passwordEncryptionProvider.encode(newPassword),
                user.getIsActive()
        ));
    }

    public User getByUsername(String username){
        return repository.getUserByUsername(username);
    }
}