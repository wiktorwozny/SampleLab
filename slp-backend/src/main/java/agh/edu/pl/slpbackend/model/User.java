package agh.edu.pl.slpbackend.model;

import agh.edu.pl.slpbackend.converter.PasswordConverter;
import agh.edu.pl.slpbackend.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@Table(name = "users")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 3180491386254929785L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    @Convert(converter = PasswordConverter.class)
    private String password;

    private RoleEnum role;
}
