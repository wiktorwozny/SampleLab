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
@Table(name = "product_group")
public class Group {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "group_indication")
    private List<Indication> indications;

    @ManyToMany
    @JoinTable(name = "group_sampling_standard")
    private  List<SamplingStandard> samplingStandards;
}
