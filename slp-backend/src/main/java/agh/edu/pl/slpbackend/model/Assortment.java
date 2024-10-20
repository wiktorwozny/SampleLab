package agh.edu.pl.slpbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
public class Assortment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JsonIgnoreProperties("assortments")
    private ProductGroup group;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Indication> indications;
}
