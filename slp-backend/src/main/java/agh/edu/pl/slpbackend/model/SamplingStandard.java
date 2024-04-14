package agh.edu.pl.slpbackend.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class SamplingStandard {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToMany
    private List<ProductGroup> groups;
}
