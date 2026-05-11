package com.example.busticket.service;

import com.example.busticket.dto.RegisterRequest;
import com.example.busticket.entity.Role;
import com.example.busticket.entity.User;
import com.example.busticket.entity.UserProfile;
import com.example.busticket.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerPassenger(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PASSENGER);

        UserProfile profile = new UserProfile();
        profile.setFullName(request.getFullName().trim());
        profile.setPhone(request.getPhone().trim());
        profile.setEmail(request.getEmail().trim());
        profile.setAddress(request.getAddress() == null ? "" : request.getAddress().trim());
        user.setProfile(profile);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsername(username.trim())
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()));
    }

    @Transactional
    public void createDefaultUser(String username, String rawPassword, Role role, String fullName, String phone, String email) {
        if (userRepository.existsByUsername(username)) {
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        UserProfile profile = new UserProfile();
        profile.setFullName(fullName);
        profile.setPhone(phone);
        profile.setEmail(email);
        profile.setAddress("");
        user.setProfile(profile);

        userRepository.save(user);
    }
}
