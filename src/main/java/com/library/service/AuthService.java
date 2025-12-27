package com.library.service;

import com.library.model.Admin;
import com.library.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin login(String username, String password) {
        log.info("Login attempt for username: {}", username);

        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            log.warn("Invalid password for username: {}", username);
            throw new RuntimeException("Invalid username or password");
        }

        log.info("Login successful for username: {}", username);
        return admin;
    }

    public Admin registerAdmin(Admin admin) {
        log.info("Registering new admin: {}", admin.getUsername());

        if (adminRepository.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        Admin savedAdmin = adminRepository.save(admin);
        log.info("Admin registered successfully: {}", savedAdmin.getUsername());

        return savedAdmin;
    }
}