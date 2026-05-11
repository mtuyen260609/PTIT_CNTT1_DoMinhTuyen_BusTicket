package com.example.busticket.config;

import com.example.busticket.entity.Role;
import com.example.busticket.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AuthService authService;

    public DataInitializer(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(String... args) {
        authService.createDefaultUser(
                "admin",
                "Admin@123",
                Role.ADMIN,
                "Admin",
                "0975219654",
                "admin@gmail.com"
        );
        authService.createDefaultUser(
                "staff",
                "Staff@123",
                Role.STAFF,
                "Nhân viên bán vé",
                "0985136952",
                "staff@gmail.com"
        );
        authService.createDefaultUser(
                "passenger",
                "Passenger@123",
                Role.PASSENGER,
                "Nguyễn Văn Tân",
                "0985125433",
                "passenger@gmail.com"
        );
    }
}
