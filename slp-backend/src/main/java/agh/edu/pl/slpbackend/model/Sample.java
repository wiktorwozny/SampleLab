package agh.edu.pl.slpbackend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Sample {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Code code;

    @ManyToOne
    private Client client;

    private String assortment;

    private LocalDate admissionDate;

    private LocalDate expirationDate;

    private String expirationDateComment;

    private LocalDate examinationEndDate;

    private String size;

    private String state;

    private boolean analysis;

    @ManyToOne
    private  Inspection inspection;

    @ManyToOne
    private Group group;

    @ManyToOne
    private SamplingStandard samplingStandard;

    @OneToOne
    private ReportData reportData;

}
