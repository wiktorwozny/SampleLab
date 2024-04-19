package agh.edu.pl.slpbackend.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Client {
    @Id
    @GeneratedValue
    private Long id;

    private String wijharsCode;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
}
