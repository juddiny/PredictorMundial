package com.worldcup.predictor.controller;

import com.worldcup.predictor.config.CustomUserDetails;
import com.worldcup.predictor.model.User;
import com.worldcup.predictor.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> profile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok(new UserProfileResponse(user.getId(), user.getName(), user.getUsername(), user.getRoles()));
    }

    @PutMapping("/update")
    public ResponseEntity<UserProfileResponse> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @Valid @RequestBody UpdateProfileRequest request) {
        User user = userDetails.getUser();
        user.setName(request.getName());
        User updated = userService.save(user);
        return ResponseEntity.ok(new UserProfileResponse(updated.getId(), updated.getName(), updated.getUsername(), updated.getRoles()));
    }

    public static class UpdateProfileRequest {
        @NotBlank
        private String name;

        public UpdateProfileRequest() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class UserProfileResponse {
        private Long id;
        private String name;
        private String username;
        private Set<String> roles;

        public UserProfileResponse() {
        }

        public UserProfileResponse(Long id, String name, String username, Set<String> roles) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.roles = roles;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Set<String> getRoles() {
            return roles;
        }

        public void setRoles(Set<String> roles) {
            this.roles = roles;
        }
    }
}
