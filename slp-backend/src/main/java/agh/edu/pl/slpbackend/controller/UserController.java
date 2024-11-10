package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.dto.UserDto;
import agh.edu.pl.slpbackend.dto.users.ChangePasswordRequest;
import agh.edu.pl.slpbackend.dto.users.LoginRequest;
import agh.edu.pl.slpbackend.dto.users.LoginResponse;
import agh.edu.pl.slpbackend.model.User;
import agh.edu.pl.slpbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) {
        User user = (User) userService.insert(userDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse user = userService.login(request);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/change-password/{email}")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request, @PathVariable String email) {
        userService.changePasswordForAdmin(request, email);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
