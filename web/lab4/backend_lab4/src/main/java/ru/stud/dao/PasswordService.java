package ru.stud.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordService {

    public String hash(String raw) {
        return BCrypt.withDefaults().hashToString(12, raw.toCharArray());
    }

    public boolean matches(String raw, String hash) {
        return BCrypt.verifyer().verify(raw.toCharArray(), hash).verified;
    }
}

