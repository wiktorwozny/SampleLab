package agh.edu.pl.slpbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Sample implements Serializable {

    @Serial
    private static final long serialVersionUID = -41275705394682306L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Code code;

    @ManyToOne
    private Client client;

    private String assortment;

    private LocalDate admissionDate;

    private LocalDate expirationDate;

    private String expirationComment;

    private LocalDate examinationEndDate;

    private String size;

    private String state;

    private boolean analysis;

    @ManyToOne
    private Inspection inspection;

    @ManyToOne
    private ProductGroup group;

    @ManyToOne
    private SamplingStandard samplingStandard;

    @OneToOne(cascade = CascadeType.ALL)
    private ReportData reportData;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sample")
    @JsonIgnoreProperties("sample")
    private List<Examination> examinations;
}
