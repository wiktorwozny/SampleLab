package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.UserController;
import agh.edu.pl.slpbackend.dto.UserDto;
import agh.edu.pl.slpbackend.dto.users.ChangePasswordRequest;
import agh.edu.pl.slpbackend.dto.users.LoginRequest;
import agh.edu.pl.slpbackend.enums.RoleEnum;
import agh.edu.pl.slpbackend.exception.AccountAlreadyExistsException;
import agh.edu.pl.slpbackend.exception.UserNotFoundException;
import agh.edu.pl.slpbackend.exception.WrongPasswordException;
import agh.edu.pl.slpbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class UserTest {

    @Autowired
    private UserController controller;

    @Autowired
    private UserRepository repository;

    private static final String WORKER_EMAIL = "worker@gmail.com";

    @Test
    @WithMockUser(roles = "ADMIN")
    void register() {
        var userDto = UserDto.builder()
                .name("Adam Nowak")
                .email("nowak@gmail.com")
                .role(RoleEnum.WORKER)
                .build();
        var response = controller.register(userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var user = response.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getRole()).isEqualTo(userDto.getRole());
        assertThat(user.getId()).isPositive();
        assertThat(user.getPassword().length()).isEqualTo(8);

        var savedUser = repository.findByEmail(userDto.getEmail()).orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void register_fails_when_email_taken() {
        var userCount = repository.count();
        var userDto = UserDto.builder()
                .name("Adam Nowak")
                .email(WORKER_EMAIL)
                .role(RoleEnum.WORKER)
                .build();

        assertThatThrownBy(() -> controller.register(userDto))
                .isInstanceOf(AccountAlreadyExistsException.class);
        assertThat(repository.count()).isEqualTo(userCount);
    }

    @Test
    void login() {
        var loginRequest = new LoginRequest(WORKER_EMAIL, "worker");
        var response = controller.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var loginResponse = response.getBody();
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.token()).isNotEmpty();

        var user = repository.findByEmail(loginRequest.email()).orElse(null);
        assertThat(user).isNotNull();
        assertThat(loginResponse.user().getName()).isEqualTo(user.getName());
        assertThat(loginResponse.user().getRole()).isEqualTo(user.getRole());
    }

    @Test
    void login_fails_when_unknown_email() {
        var loginRequest = new LoginRequest("", "");

        assertThatThrownBy(() -> controller.login(loginRequest))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void login_fails_when_wrong_password() {
        var loginRequest = new LoginRequest(WORKER_EMAIL, "");

        assertThatThrownBy(() -> controller.login(loginRequest))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void get_all() {
        var response = controller.getUsers();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var users = response.getBody();
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(repository.count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete() {
        assertThat(repository.existsByEmail(WORKER_EMAIL)).isTrue();
        var count = repository.count();

        var response = controller.deleteUser(WORKER_EMAIL);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.existsByEmail(WORKER_EMAIL)).isFalse();
        assertThat(repository.count()).isEqualTo(count - 1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_fails_when_unknown_email() {
        assertThatThrownBy(() -> controller.deleteUser(""))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void change_password() {
        var user = repository.findByEmail(WORKER_EMAIL)
                .orElseThrow();
        var newPassword = "test";
        var request = new ChangePasswordRequest(user.getPassword(), newPassword);

        var response = controller.changePassword(request, WORKER_EMAIL);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(user.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void change_password_fails_when_unknown_email() {
        var user = repository.findByEmail(WORKER_EMAIL)
                .orElseThrow();
        var newPassword = "test";
        var request = new ChangePasswordRequest(user.getPassword(), newPassword);

        assertThatThrownBy(() -> controller.changePassword(request, ""))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void change_password_fails_when_wrong_password() {
        var newPassword = "test";
        var request = new ChangePasswordRequest("", newPassword);

        assertThatThrownBy(() -> controller.changePassword(request, WORKER_EMAIL))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void change_password_by_admin() {
        var user = repository.findByEmail(WORKER_EMAIL)
                .orElseThrow();
        var newPassword = "test";
        var request = new ChangePasswordRequest("", newPassword);

        var response = controller.changePasswordByAdmin(request, WORKER_EMAIL);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(user.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void change_password_by_admin_fails_when_unknown_email() {
        var newPassword = "test";
        var request = new ChangePasswordRequest("", newPassword);

        assertThatThrownBy(() -> controller.changePasswordByAdmin(request, ""))
                .isInstanceOf(UserNotFoundException.class);
    }
}
