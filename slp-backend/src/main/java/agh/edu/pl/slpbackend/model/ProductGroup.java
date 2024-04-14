package agh.edu.pl.slpbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class ProductGroup {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToMany
    private List<Indication> indications;
    @ManyToMany
    private  List<SamplingStandard> samplingStandards;
}
