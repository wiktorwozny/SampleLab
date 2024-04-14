package agh.edu.pl.slpbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue
    private Long id;
    private String wijharsCode;
    private String name;
    @OneToOne
    private Address address;
}
