package agh.edu.pl.slpbackend.dto.users;

import agh.edu.pl.slpbackend.dto.UserDto;

public record LoginResponse(UserDto user, String token) {
}
