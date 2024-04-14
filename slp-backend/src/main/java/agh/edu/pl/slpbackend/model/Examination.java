package agh.edu.pl.slpbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Indication indication;

    @ManyToOne
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

    @Size(max = 2)
    private float uncertainty;

    private float lod;

    private float loq;
}
