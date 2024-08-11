package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.dto.users.LoginRequest;
import agh.edu.pl.slpbackend.dto.users.UserDto;
import agh.edu.pl.slpbackend.model.User;
import agh.edu.pl.slpbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
    @RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    public final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) {
        User user = (User) userService.insert(userDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {
        UserDto user = userService.login(request);
        return ResponseEntity.ok(user);
    }
}
