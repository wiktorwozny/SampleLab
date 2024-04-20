package agh.edu.pl.slpbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
public class Client implements Serializable {
    @Serial
    private static final long serialVersionUID = 854868781531586203L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String wijharsCode;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
}
