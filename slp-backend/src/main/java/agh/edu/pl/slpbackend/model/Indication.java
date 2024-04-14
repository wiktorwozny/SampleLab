package agh.edu.pl.slpbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class Indication {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String norm;
    private String unit;
    private String laboratory;
    @ManyToMany
    private List<ProductGroup> groups;
}
