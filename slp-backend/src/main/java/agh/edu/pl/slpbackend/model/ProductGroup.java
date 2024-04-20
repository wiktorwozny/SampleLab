package agh.edu.pl.slpbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Table(name = "product_group")
public class ProductGroup implements Serializable {
    @Serial
    private static final long serialVersionUID = -6720720397053849550L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "group_indication")
    private List<Indication> indications;

    @ManyToMany
    @JoinTable(name = "group_sampling_standard")
    private List<SamplingStandard> samplingStandards;
}
