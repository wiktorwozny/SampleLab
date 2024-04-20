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
public class Indication implements Serializable {

    @Serial
    private static final long serialVersionUID = 6438980277477269399L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String norm;

    private String unit;

    private String laboratory;

    @ManyToMany(mappedBy = "indications")
    private List<ProductGroup> groups;
}
