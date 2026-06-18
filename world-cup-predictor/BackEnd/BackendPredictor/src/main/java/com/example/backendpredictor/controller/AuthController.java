package com.example.backendpredictor.controller;

import com.example.backendpredictor.entity.Role;
import com.example.backendpredictor.entity.User;
import com.example.backendpredictor.repository.RoleRepository;
import com.example.backendpredictor.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String name = body.getOrDefault("name", "");
        String username = body.getOrDefault("username", "");
        String password = body.getOrDefault("password", "");

        if (name.isBlank() || username.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Nombre, username y password son requeridos"));
        }

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(Map.of("message", "El username ya existe"));
        }

        Role role = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        User user = new User(name, username, passwordEncoder.encode(password));
        user.getRoles().add(role);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Usuario registrado con éxito", "username", username));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "");
        String password = body.getOrDefault("password", "");

        if (username.isBlank() || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "username y password son requeridos"));
        }

        return userRepository.findByUsername(username).map(user -> {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciales inválidas"));
            }
            // generar token simple (no firmado) para uso en frontend; cambiar a JWT si se desea
            String token = "token." + Base64.getEncoder().encodeToString((username + ":" + System.currentTimeMillis()).getBytes());
            var roles = user.getRoles().stream().map(Role::getName).toList();
            return ResponseEntity.ok(Map.<String,Object>of(
                    "message", "Login exitoso",
                    "username", user.getUsername(),
                    "name", user.getName(),
                    "roles", roles,
                    "token", token
            ));
        }).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciales inválidas")));
    }
}

