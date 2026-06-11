package com.worldcup.predictor.config;

import com.worldcup.predictor.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        createUserIfMissing("Administrador", "admin", "admin123", "ROLE_ADMIN", "ROLE_USER");
        createUserIfMissing("Usuario Regular", "user", "user123", "ROLE_USER");
    }

    private void createUserIfMissing(String name, String username, String password, String... roles) {
        if (userService.findByUsername(username).isPresent()) {
            return;
        }
        userService.createUser(name, username, password, java.util.Set.of(roles));
    }
}
