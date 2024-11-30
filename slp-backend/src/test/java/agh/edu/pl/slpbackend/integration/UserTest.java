package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.UserController;
import agh.edu.pl.slpbackend.dto.UserDto;
import agh.edu.pl.slpbackend.enums.RoleEnum;
import agh.edu.pl.slpbackend.exception.AccountAlreadyExistsException;
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

    @Test
    @WithMockUser(roles = "ADMIN")
    public void register() {
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
    public void register_fails_when_email_taken() {
        var userCount = repository.count();
        var userDto = UserDto.builder()
                .name("Adam Nowak")
                .email("worker@gmail.com")
                .role(RoleEnum.WORKER)
                .build();

        assertThatThrownBy(() -> controller.register(userDto))
                .isInstanceOf(AccountAlreadyExistsException.class);
        assertThat(repository.count()).isEqualTo(userCount);
    }
}
