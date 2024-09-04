package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.UserDto;
import agh.edu.pl.slpbackend.model.User;
import org.apache.commons.lang3.RandomStringUtils;

public interface UserMapper {

    default User toModel(final UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .password(RandomStringUtils.randomAlphanumeric(8))
                .build();
    }

    default UserDto toDto(final User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
