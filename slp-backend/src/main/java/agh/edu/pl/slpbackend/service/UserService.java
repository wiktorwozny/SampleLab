package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.auth.JwtUtil;
import agh.edu.pl.slpbackend.dto.UserDto;
import agh.edu.pl.slpbackend.dto.users.ChangePasswordForAdminRequest;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService extends AbstractService implements UserMapper {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public Object insert(IModel model) {
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

    }

    public LoginResponse login(LoginRequest request) {
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
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(request.oldPassword())) {
            throw new WrongPasswordException();
        }

        user.setPassword(request.newPassword());
        userRepository.save(user);
    }

    public void changePasswordForAdmin(ChangePasswordRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setPassword(request.newPassword());
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
