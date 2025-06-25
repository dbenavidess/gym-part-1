package com.dbenavidess.gym_part_1.infrastructure.util;

import com.dbenavidess.gym_part_1.domain.util.PasswordEncryptionProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityEncryptionProvider implements PasswordEncryptionProvider {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
        return passwordEncoder.matches(password,encodedPassword);
    }

    @Override
    public PasswordEncoder getPasswordEncoder(){
        return passwordEncoder;
    }


}
