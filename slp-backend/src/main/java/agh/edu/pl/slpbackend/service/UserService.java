package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.users.LoginRequest;
import agh.edu.pl.slpbackend.dto.users.UserDto;
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

@Service
@AllArgsConstructor
public class UserService extends AbstractService implements UserMapper {

    private final UserRepository userRepository;

    @Override
    public Object insert(IModel model) {
        final UserDto userDto = (UserDto) model;
        if (userRepository.existsByEmail(userDto.email())) {
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

    public UserDto login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(request.password())) {
            throw new WrongPasswordException();
        }
        return toDto(user);
    }
}
