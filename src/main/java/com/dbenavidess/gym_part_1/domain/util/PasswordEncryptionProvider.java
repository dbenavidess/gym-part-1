package com.dbenavidess.gym_part_1.domain.util;


import org.springframework.security.crypto.password.PasswordEncoder;

public interface PasswordEncryptionProvider {
    String encode(String password);
    boolean matches(String password, String encodedPassword);
    PasswordEncoder getPasswordEncoder();


}
