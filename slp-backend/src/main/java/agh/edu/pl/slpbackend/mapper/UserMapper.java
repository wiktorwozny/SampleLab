package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.users.UserDto;
import agh.edu.pl.slpbackend.model.User;
import org.apache.commons.lang3.RandomStringUtils;

public interface UserMapper {

    default User toModel(final UserDto userDto) {
        return User.builder()
                .name(userDto.name())
                .email(userDto.email())
                .role(userDto.role())
                .password(RandomStringUtils.randomAlphanumeric(8))
                .build();
    }
}
