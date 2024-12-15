package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.auth.JwtUtil;
import agh.edu.pl.slpbackend.dto.UserDto;
import agh.edu.pl.slpbackend.dto.users.ChangePasswordRequest;
import agh.edu.pl.slpbackend.dto.users.LoginRequest;
import agh.edu.pl.slpbackend.dto.users.LoginResponse;
import agh.edu.pl.slpbackend.exception.AccountAlreadyExistsException;
import agh.edu.pl.slpbackend.exception.UserNotFoundException;
import agh.edu.pl.slpbackend.exception.WrongPasswordException;
import agh.edu.pl.slpbackend.mapper.UserMapper;
import agh.edu.pl.slpbackend.model.User;
import agh.edu.pl.slpbackend.repository.UserRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService extends AbstractService implements UserMapper {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public Object insert(IModel model) {
        log.info("insert user");
        final UserDto userDto = (UserDto) model;
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new AccountAlreadyExistsException();
        }
        final User user = toModel(userDto);
        return userRepository.save(user);
    }

    @Override
    public Object update(IModel model) {
        return null;
    }

    @Override
    public void delete(IModel model) {
        log.info("delete user");
        final UserDto userDto = (UserDto) model;
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(user.getId());
    }

    public LoginResponse login(LoginRequest request) {
        log.info("login user");
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(request.password())) {
            throw new WrongPasswordException();
        }

        UserDto userDto = toDto(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(userDto, token);
    }

    public void changePassword(ChangePasswordRequest request, String email) {
        log.info("change password");
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(request.oldPassword())) {
            throw new WrongPasswordException();
        }

        user.setPassword(request.newPassword());
        userRepository.save(user);
    }

    public void changePasswordByAdmin(ChangePasswordRequest request, String email) {
        log.info("change password by admin");
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setPassword(request.newPassword());
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        log.info("get all users");
        return userRepository.findAll();
    }
}
