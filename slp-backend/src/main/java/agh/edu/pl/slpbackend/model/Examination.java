package agh.edu.pl.slpbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Indication indication;

    @ManyToOne
    @JsonIgnoreProperties("examinations")
    private Sample sample;

    private String signage;
    
    private String nutritionalValue;

    private String specification;

    private String regulation;

    private int samplesNumber;

    private String result;

    private LocalDate startDate;

    private LocalDate endDate;

    private String methodStatus;

    //@Size(max = 2)
    private float uncertainty;

    private float lod;

    private float loq;
}
