package agh.edu.pl.slpbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Indication {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String norm;

    private String unit;

    private String laboratory;

    @ManyToMany(mappedBy = "indications")
    private List<Group> groups;
}
