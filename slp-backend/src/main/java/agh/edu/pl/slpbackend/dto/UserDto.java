package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.enums.RoleEnum;
import agh.edu.pl.slpbackend.model.User;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ModelClass(User.class)
public class UserDto implements IModel {
    private String name;
    private String email;
    private RoleEnum role;
}
