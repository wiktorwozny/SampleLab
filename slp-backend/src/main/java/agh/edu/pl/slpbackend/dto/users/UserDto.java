package agh.edu.pl.slpbackend.dto.users;

import agh.edu.pl.slpbackend.enums.RoleEnum;
import agh.edu.pl.slpbackend.model.User;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import lombok.Builder;

@ModelClass(User.class)
@Builder
public record UserDto(String name, String email, RoleEnum role) implements IModel {
}