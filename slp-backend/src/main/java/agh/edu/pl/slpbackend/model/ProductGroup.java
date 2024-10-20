package agh.edu.pl.slpbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ProductGroup implements Serializable {
    @Serial
    private static final long serialVersionUID = -6720720397053849550L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany
    @JsonIgnore
    private List<SamplingStandard> samplingStandards;

    @OneToMany(mappedBy = "group")
    private List<Assortment> assortments;
}
