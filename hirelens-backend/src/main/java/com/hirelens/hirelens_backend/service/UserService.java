
package com.hirelens.hirelens_backend.service;

import com.hirelens.hirelens_backend.dto.UserRegistrationRequest;
import com.hirelens.hirelens_backend.dto.UserRegistrationResponse;
import com.hirelens.hirelens_backend.entity.User;
import com.hirelens.hirelens_backend.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserRegistrationResponse registerUser(
            UserRegistrationRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);

        // Never store a plain-text password.
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return new UserRegistrationResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                "Registration successful"
        );
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(
                email.trim().toLowerCase()
        );
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(
                email.trim().toLowerCase()
        );
    }
}
